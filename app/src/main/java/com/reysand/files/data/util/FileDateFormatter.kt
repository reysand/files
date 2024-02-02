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
package com.reysand.files.data.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Utility class for formatting file dates.
 */
object FileDateFormatter {

    /**
     * Converts the given date to a Unix timestamp.
     *
     * @param date The date to convert.
     * @return The Unix timestamp.
     */
    fun convertToUnixTimestamp(date: String): Long {
        var formattedDate = date
        if (date.contains(".")) {
            val fractionalSeconds = date.split(".")[1].split("Z")[0]
            if (fractionalSeconds.length < 3) {
                formattedDate = date.replace(".$fractionalSeconds", ".$fractionalSeconds"
                        .padEnd(4, '0'))
            }
        } else {
            formattedDate = date.replace("Z", ".000Z")
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val timestamp = inputFormat.parse(formattedDate)
        return timestamp?.time ?: 0
    }

    /**
     * Gets the last modified date in a human-readable format.
     *
     * @param lastModified The timestamp of the last modification.
     * @return A string representing the last modified date (e.g., "Sep 12, 2023).
     */
    fun getLastModified(lastModified: Long): String {
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