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

import android.os.Environment
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.reysand.files.FilesApplication
import com.reysand.files.data.model.FileModel
import com.reysand.files.data.repository.FileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel for managing file-related data and operations.
 *
 * @param fileRepository The repository for accessing file data.
 */
class FilesViewModel(private val fileRepository: FileRepository) : ViewModel() {

    // MutableStateFlow holding the list of files
    private var _files = MutableStateFlow<List<FileModel>>(emptyList())
    val files = _files.asStateFlow()

    // Paths for the home and current directories
    val homeDirectory = Environment.getExternalStorageDirectory().path
    val currentDirectory = mutableStateOf(homeDirectory)

    // State indicating whether to show the permission dialog
    val showPermissionDialog = mutableStateOf(!Environment.isExternalStorageManager())

    // Initialize the ViewModel by loading files from the home directory
    init {
        getFiles(homeDirectory)
    }

    /**
     * Get a list of files from the specified path.
     *
     * @param path The path to the directory containing the files.
     */
    fun getFiles(path: String) {
        viewModelScope.launch {
            _files.value = fileRepository.getFiles(path)
            currentDirectory.value = path
        }
    }

    /**
     * Navigate up to the parent directory,
     */
    fun navigateUp() {
        val parentDirectory = File(currentDirectory.value).parent

        if (currentDirectory.value != homeDirectory && parentDirectory != null) {
            getFiles(parentDirectory)
        }
    }

    companion object {
        // Factory for creating FilesViewModel instances
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FilesApplication)
                val fileRepository = application.container.fileRepository
                FilesViewModel(fileRepository = fileRepository)
            }
        }
    }
}