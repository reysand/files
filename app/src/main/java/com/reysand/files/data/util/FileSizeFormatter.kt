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
package com.reysand.files.data.util

import kotlin.math.log10
import kotlin.math.pow

/**
 * Utility class for formatting file sizes.
 */
object FileSizeFormatter {

    /**
     * Formats the given size in bytes to a human-readable format.
     *
     * @param size The size in bytes.
     * @return A string representing the formatted size (e.g., "2.5 MB").
     */
    fun getFormattedSize(size: Long): String {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val base = 1024.0

        val unitIndex = (log10(size.toDouble()) / log10(base)).toInt().coerceIn(0, units.size - 1)
        val convertedSize = size / base.pow(unitIndex.toDouble())

        return String.format("%.2f %s", convertedSize, units[unitIndex])
    }
}