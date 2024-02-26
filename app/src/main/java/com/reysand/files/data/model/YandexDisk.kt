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
package com.reysand.files.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a Yandex.Disk object.
 *
 * @property embedded The embedded files.
 */
data class YandexDisk(
    @SerializedName("_embedded")
    val embedded: Embedded
)

/**
 * Data class representing the embedded files from Yandex.Disk.
 *
 * @property items The list of files.
 */
data class Embedded(
    val items: List<YandexDiskFile>
)

/**
 * Data class representing a file from Yandex.Disk.
 *
 * @property name The name of the file.
 * @property type The type of the file.
 * @property path The path of the file.
 * @property size The size of the file in bytes.
 * @property modified The timestamp of the last modification in ISO8601.
 */
data class YandexDiskFile(
    val name: String,
    val type: String,
    val path: String,
    val size: Long,
    val modified: String
)
