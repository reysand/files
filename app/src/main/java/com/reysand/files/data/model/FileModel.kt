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
package com.reysand.files.data.model

import com.reysand.files.data.util.FileDateFormatter
import com.reysand.files.data.util.FileSizeFormatter

/**
 * Data class representing a file.
 *
 * @property name The name of the file.
 * @property path The path of the file.
 * @property fileType The type of the file.
 * @property size The size of the file in bytes.
 * @property lastModified The timestamp of the last modification.
 */
data class FileModel(
    val name: String,
    val path: String,
    val fileType: FileType,
    val size: Long,
    val lastModified: Long
) {
    /**
     * Enum class representing the type of file.
     */
    enum class FileType {
        DIRECTORY,
        OTHER
    }

    fun getFormattedSize(): String = FileSizeFormatter.getFormattedSize(size)

    fun getLastModified(): String = FileDateFormatter.getLastModified(lastModified)
}
