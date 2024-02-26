/*
 * Copyright 2023 Andrey Slyusar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reysand.files.ui.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reysand.files.FilesApplication
import com.reysand.files.R
import com.reysand.files.data.model.FileModel
import com.reysand.files.data.repository.FileRepository
import com.reysand.files.data.repository.OneDriveRepository
import com.reysand.files.data.repository.YandexUserRepository
import com.reysand.files.data.util.MicrosoftService
import com.reysand.files.data.util.YandexService
import com.reysand.files.ui.util.ContextWrapper
import com.yandex.authsdk.YandexAuthResult
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "FilesViewModel"

/**
 * ViewModel for managing file-related data and operations.
 *
 * @param fileRepository The repository for accessing file data.
 * @param contextWrapper The wrapper for accessing the application context.
 */
class FilesViewModel(
    private val fileRepository: FileRepository,
    private val oneDriveRepository: OneDriveRepository,
    private val yandexUserRepository: YandexUserRepository,
    private val contextWrapper: ContextWrapper,
    private val microsoftService: MicrosoftService,
    private val yandexService: YandexService
) : ViewModel() {

    // MutableStateFlow holding the list of files
    private var _files = MutableStateFlow<List<FileModel>>(emptyList())
    val files = _files.asStateFlow()

    // Paths for the home and current directories
    var homeDirectory = Environment.getExternalStorageDirectory().path
    val currentDirectory = mutableStateOf(homeDirectory)

    // State indicating the current data source
    private var _currentStorage = MutableStateFlow("Local")
    val currentStorage = _currentStorage.asStateFlow()

    // State indicating whether to show the permission dialog
    val showPermissionDialog = mutableStateOf(!Environment.isExternalStorageManager())

    val oneDriveAccount = mutableStateOf<String?>(null)
    val yandexDiskAccount = mutableStateOf<String?>(null)

    // Initialize the ViewModel by loading files from the home directory
    init {
        getFiles(homeDirectory)

        viewModelScope.launch {
            microsoftService.acquireTokenSilently()
            microsoftService.usernameFlow.collect { username ->
                oneDriveAccount.value = username
                Log.d("FilesViewModel", "oneDriveAccount: ${oneDriveAccount.value}")
            }
//            yandexDiskAccount.value = yandexUserRepository.getInfo(yandexService.token?.value)
        }
    }

    fun setCurrentStorage(storage: String) {
        _currentStorage.value = storage
        when (storage) {
            "Local" -> homeDirectory = Environment.getExternalStorageDirectory().path
            "OneDrive" -> homeDirectory = "/"
        }
        currentDirectory.value = homeDirectory
        getFiles(homeDirectory)
    }

    /**
     * Toggle the Microsoft sign-in state.
     *
     * @param context The context of the app.
     */
    suspend fun toggleMicrosoftSignIn(context: Context) {
        if (microsoftService.isSignedIn()) {
            microsoftService.signOut()
            oneDriveAccount.value = null
        } else {
            microsoftService.signIn(context) { account ->
                oneDriveAccount.value = account
            }
        }
    }

    fun handleResult(result: YandexAuthResult) {
        yandexService.handleResult(result)
    }

    suspend fun getInfo() {
        if (yandexService.token != null) {
            Log.d(TAG, "token: ${yandexService.token!!.value}")
            Log.d(TAG, "getInfo: ${yandexUserRepository.getInfo(yandexService.token!!.value)}")
            yandexDiskAccount.value = yandexUserRepository.getInfo(yandexService.token!!.value)
        }

//        Log.d(TAG, "getInfo: ${yandexUserRepository.getInfo(yandexService.token!!)}")
    }

    /**
     * Get a list of files from the specified path.
     *
     * @param path The path to the directory containing the files.
     */
    fun getFiles(path: String) {
        viewModelScope.launch {
            when (currentStorage.value) {
                "Local" -> _files.value = fileRepository.getFiles(path)
                "OneDrive" -> _files.value = oneDriveRepository.getFiles(path)
            }
            currentDirectory.value = path
        }
    }

    /**
     * Navigate up to the parent directory,
     */
    fun navigateUp() {
        val parentDirectory = when (currentStorage.value) {
            "Local" -> File(currentDirectory.value).parent
            "OneDrive" -> if (!currentDirectory.value.contains('/')) {
                "/"
            } else {
                currentDirectory.value.substringBeforeLast('/')
            }

            else -> null
        }

        if (currentDirectory.value != homeDirectory && parentDirectory != null) {
            getFiles(parentDirectory)
        }
    }

    /**
     * Gets the free space of the storage.
     *
     * @return A string representing the free space of the storage.
     */
    fun getStorageFreeSpace(): String {
        return contextWrapper.getContext().getString(
            R.string.storage_free_space,
            fileRepository.getStorageFreeSpace()
        )
    }

    /**
     * Gets the free space of the OneDrive storage.
     *
     * @return A string representing the free space of the OneDrive storage.
     */
    suspend fun getOneDriveStorageFreeSpace(): String {
        return viewModelScope.async {
            contextWrapper.getContext().getString(
                R.string.storage_free_space,
                oneDriveRepository.getStorageFreeSpace()
            )
        }.await()
    }

    /**
     * Move a file from one path to another.
     *
     * @param file The file to move.
     * @param destination The destination path.
     */
    fun moveFile(file: FileModel, destination: String) {
        viewModelScope.launch {
            val result = when (currentStorage.value) {
                "Local" -> fileRepository.moveFile(file.path, homeDirectory.plus(destination))
                else -> oneDriveRepository.moveFile(
                    file.path,
                    homeDirectory.plus(destination.substringBeforeLast('/'))
                )
            }

            if (result) {
                getFiles(currentDirectory.value)
            }
        }
    }

    /**
     * Copy a file from one path to another.
     *
     * @param file The file to copy.
     * @param destination The destination path.
     */
    fun copyFile(file: FileModel, destination: String) {
        viewModelScope.launch {
            val result = when (currentStorage.value) {
                "Local" -> fileRepository.copyFile(file.path, homeDirectory.plus(destination))
                else -> oneDriveRepository.copyFile(
                    file.path,
                    homeDirectory.plus(destination.substringBeforeLast('/'))
                )
            }

            Log.d(TAG, "copyFile: ${file.path}")

            if (result) {
                getFiles(currentDirectory.value)
            }
        }
    }

    /**
     * Rename a file.
     *
     * @param file The file to rename.
     * @param newName The new name of the file.
     */
    fun renameFile(file: FileModel, newName: String) {
        viewModelScope.launch {
            val result = when (currentStorage.value) {
                "Local" -> fileRepository.renameFile(
                    file.path,
                    file.path.removeSuffix(file.name).plus(newName)
                )

                else -> oneDriveRepository.renameFile(file.path, newName)
            }

            if (result) {
                getFiles(currentDirectory.value)
            }
        }
    }

    /**
     * Delete a file.
     *
     * @param path The path of the file to delete.
     */
    fun deleteFile(path: String) {
        viewModelScope.launch {
            val result = when (currentStorage.value) {
                "Local" -> fileRepository.deleteFile(path)
                else -> oneDriveRepository.deleteFile(path)
            }

            if (result) {
                getFiles(currentDirectory.value)
            }
        }
    }

    companion object {
        // Factory for creating FilesViewModel instances
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FilesApplication)
                val fileRepository = application.container.fileRepository
                val oneDriveRepository = application.container.oneDriveRepository
                val yandexUserRepository = application.container.yandexUserRepository
                val contextWrapper = ContextWrapper(application.applicationContext)
                val microsoftService = application.container.microsoftService
                val yandexService = application.container.yandexService
                FilesViewModel(
                    fileRepository = fileRepository,
                    oneDriveRepository = oneDriveRepository,
                    yandexUserRepository = yandexUserRepository,
                    contextWrapper = contextWrapper,
                    microsoftService = microsoftService,
                    yandexService = yandexService
                )
            }
        }
    }
}