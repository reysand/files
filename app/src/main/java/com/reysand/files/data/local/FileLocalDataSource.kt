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
package com.reysand.files.data.local

import com.reysand.files.data.model.FileModel
import com.reysand.files.data.repository.FileRepository
import java.io.File

/**
 * Implementation of [FileRepository] for accessing local files.
 */
class FileLocalDataSource : FileRepository {

    override suspend fun getFiles(path: String): List<FileModel> {
        // List files in the specified path
        val files = File(path).listFiles()

        // Separate directories and other files
        val directories = mutableListOf<FileModel>()
        val others = mutableListOf<FileModel>()

        files?.forEach { file ->
            val fileModel = createFileModel(file)
            when (fileModel.fileType) {
                FileModel.FileType.DIRECTORY -> directories.add(fileModel)
                else -> others.add(fileModel)
            }
        }

        // Sort directories and other files separately
        directories.sortBy { it.name.lowercase() }
        others.sortBy { it.name.lowercase() }

        // Combine and return the sorted list
        return directories + others
    }

    /**
     * Creates a [FileModel] object from a [File] instance.
     *
     * @param file The [File] instance to create a [FileModel] from.
     * @return A [FileModel] object representing the given file.
     */
    private fun createFileModel(file: File): FileModel {
        return FileModel(
            name = file.name,
            path = file.path,
            fileType = when (file.isDirectory) {
                true -> FileModel.FileType.DIRECTORY
                else -> FileModel.FileType.OTHER
            },
            size = file.length(),
            lastModified = file.lastModified()
        )
    }
}