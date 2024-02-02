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
package com.reysand.files.data.repository

import com.reysand.files.data.model.FileModel

/**
 * Interface defining operations for interacting with files from OneDrive.
 */
interface OneDriveRepository {

    /**
     * Retrieves the free space of the storage.
     *
     * @return A formatted string containing the free space of the storage.
     */
    suspend fun getStorageFreeSpace(): String

    /**
     * Retrieves a list of [FileModel] objects from the specified path.
     *
     * @param path The path to the directory containing the files.
     * @return A list of [FileModel] objects.
     */
    suspend fun getFiles(path: String): List<FileModel>

    /**
     * Move a file from one path to another.
     *
     * @param source The original path of the file.
     * @param destination The new path of the file.
     * @return A boolean value indicating the success of the operation.
     */
    suspend fun moveFile(source: String, destination: String): Boolean

    /**
     * Copy a file from one path to another.
     *
     * @param source The original path of the file.
     * @param destination The new path of the file.
     * @return A boolean value indicating the success of the operation.
     */
    suspend fun copyFile(source: String, destination: String): Boolean

    /**
     * Rename a file.
     *
     * @param from The original path of the file.
     * @param to The new path of the file.
     * @return A boolean value indicating the success of the operation.
     */
    suspend fun renameFile(from: String, to: String): Boolean

    /**
     * Delete a file.
     *
     * @param path The path of the file to delete.
     * @return A boolean value indicating the success of the operation.
     */
    suspend fun deleteFile(path: String): Boolean
}