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

import java.util.Calendar
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

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

    /**
     * Gets a human-readable formatted size.
     *
     * @return A string representing the formatted size (e.g., "2.5 MB").
     */
    fun getFormattedSize(): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val base = 1024.0

        val unitIndex = (log10(size.toDouble()) / log10(base)).toInt().coerceIn(0, units.size - 1)
        val convertedSize = size / base.pow(unitIndex.toDouble())

        return String.format("%.2f %s", convertedSize, units[unitIndex])
    }

    /**
     * Gets the last modified date in a human-readable format.
     *
     * @return A string representing the last modified date (e.g., "Sep 12, 2023).
     */
    fun getLastModified(): String {
        val current = Calendar.getInstance()
        val date = Calendar.getInstance().apply { timeInMillis = lastModified }

        val displayMonth = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        val dayOfMonth = date.get(Calendar.DAY_OF_MONTH)

        val formattedDate = buildString {
            append("$displayMonth $dayOfMonth")
            if (date.get(Calendar.YEAR) != current.get(Calendar.YEAR)) {
                append(", ${date.get(Calendar.YEAR)}")
            }
        }
        return formattedDate
    }
}
