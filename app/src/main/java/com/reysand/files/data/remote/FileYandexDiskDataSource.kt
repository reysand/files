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
import com.reysand.files.data.model.YandexDiskFile
import com.reysand.files.data.repository.YandexDiskRepository
import com.reysand.files.data.util.FileDateFormatter
import com.reysand.files.data.util.FileSizeFormatter
import com.reysand.files.data.util.YandexDiskService
import com.reysand.files.data.util.YandexService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "FileYandexDiskDataSource"

class FileYandexDiskDataSource(private val yandexService: YandexService) : YandexDiskRepository {

    private val yandexAPIService: YandexDiskService by lazy {
        Retrofit.Builder()
            .baseUrl("https://cloud-api.yandex.net/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YandexDiskService::class.java)
    }

    override suspend fun getStorageFreeSpace(): String {
        var freeSpace = 0L

        try {
            val response = yandexAPIService.getDisk(yandexService.token?.value ?: "")
            Log.d(TAG, "YandexService: $response")

            if (response.isSuccessful) {
                freeSpace = (response.body()?.total ?: 0) - (response.body()?.used ?: 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return FileSizeFormatter.getFormattedSize(freeSpace)
    }

    override suspend fun getFiles(path: String): List<FileModel> {
        val files = mutableListOf<FileModel>()

        try {
            val response = yandexAPIService.getFiles(yandexService.token?.value ?: "", path)
            Log.d(TAG, "YandexService: $response")

            if (response.isSuccessful) {
                for (item in response.body()?.embedded?.items!!) {
                    files.add(createFileModel(item))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return files
    }

    override suspend fun moveFile(source: String, destination: String): Boolean {
        val response = yandexAPIService.moveFile(
            yandexService.token?.value ?: "",
            source,
            destination
        )
        Log.d(TAG, "YandexService: $response")

        return response.isSuccessful
    }

    override suspend fun copyFile(source: String, destination: String): Boolean {
        val response = yandexAPIService.copyFile(
            yandexService.token?.value ?: "",
            source,
            destination
        )
        Log.d(TAG, "YandexService: $response")

        return response.isSuccessful
    }

    override suspend fun renameFile(from: String, to: String): Boolean {
        val response = yandexAPIService.moveFile(
            yandexService.token?.value ?: "",
            from,
            if (from.contains('/')) {
                from.substringBeforeLast('/') + "/" + to
            } else {
                to
            }
        )
        Log.d(TAG, "YandexService: $response")

        return response.isSuccessful
    }

    override suspend fun deleteFile(path: String): Boolean {
        val response = yandexAPIService.deleteFile(yandexService.token?.value ?: "", path)
        Log.d(TAG, "YandexService: $response")

        return response.isSuccessful
    }

    /**
     * Creates a [FileModel] object from a [YandexDiskFile] instance.
     *
     * @param file The [YandexDiskFile] instance to create a [FileModel] from.
     * @return A [FileModel] object representing the given file.
     */
    private fun createFileModel(file: YandexDiskFile): FileModel {
        return FileModel(
            name = file.name,
            path = getPath(file.path),
            fileType = if (file.type == "dir") FileModel.FileType.DIRECTORY else FileModel.FileType.OTHER,
            size = file.size,
            lastModified = FileDateFormatter.convertToUnixTimestamp(file.modified)
        )
    }

    /**
     * Generate a new path for a file.
     * Removes the "disk:/" part of the path.
     *
     * @param path The path of the file.
     */
    private fun getPath(path: String): String {
        return path.substringAfterLast(":")
    }
}