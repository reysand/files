/*
 * Copyright 2024 Andrey Slyusar
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
package com.reysand.files.data.remote

import android.util.Log
import com.reysand.files.data.model.FileModel
import com.reysand.files.data.model.OneDriveFile
import com.reysand.files.data.repository.OneDriveRepository
import com.reysand.files.data.util.FileDateFormatter
import com.reysand.files.data.util.FileSizeFormatter
import com.reysand.files.data.util.MicrosoftService
import com.reysand.files.data.util.OneDriveService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FileOneDriveDataSource"

@OptIn(DelicateCoroutinesApi::class)
class FileOneDriveDataSource(private val microsoftService: MicrosoftService) : OneDriveRepository {

    private val oneDriveService: OneDriveService by lazy {
        Retrofit.Builder()
            .baseUrl("https://graph.microsoft.com/v1.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OneDriveService::class.java)
    }

    init {
        GlobalScope.launch {
            microsoftService.acquireTokenSilently()
        }
    }

    override suspend fun getStorageFreeSpace(): String {
        var freeSpace = 0L

        try {
            val response = oneDriveService.getDrive("Bearer ${microsoftService.mAccessToken}")
            Log.d(TAG, "OneDriveService: $response")

            if (response.isSuccessful) {
                freeSpace = response.body()?.quota?.remaining ?: 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return FileSizeFormatter.getFormattedSize(freeSpace)
    }

    override suspend fun getFiles(path: String): List<FileModel> {
        val files = mutableListOf<FileModel>()

        try {
            val response = oneDriveService.getFiles("Bearer ${microsoftService.mAccessToken}", path)
            Log.d(TAG, "response: $response")

            if (response.isSuccessful) {
                for (item in response.body()?.value!!) {
                    files.add(createFileModel(item))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return files
    }

    override suspend fun moveFile(source: String, destination: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun copyFile(source: String, destination: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun renameFile(from: String, to: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFile(path: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Creates a [FileModel] object from a [OneDriveFile] instance.
     *
     * @param file The [OneDriveFile] instance to create a [FileModel] from.
     * @return A [FileModel] object representing the given file.
     */
    private fun createFileModel(file: OneDriveFile): FileModel {
        return FileModel(
            name = file.name,
            path = getPath(file.name, file.parentReference.path),
            fileType = if (file.folder != null) FileModel.FileType.DIRECTORY else FileModel.FileType.OTHER,
            size = file.size,
            lastModified = FileDateFormatter.convertToUnixTimestamp(file.lastModifiedDateTime)
        )
    }

    /**
     * Generate a new path for a file.
     * Removes the "/drive/root:" part of the path.
     *
     * @param file The file to generate a new path for.
     * @param path The path of the file.
     */
    private fun getPath(file: String, path: String): String {
        var newPath: String = path.substringAfterLast(":")
        newPath = if (newPath.isEmpty()) file else "$newPath/$file"

        return if (newPath.startsWith('/')) newPath.drop(1) else newPath
    }
}