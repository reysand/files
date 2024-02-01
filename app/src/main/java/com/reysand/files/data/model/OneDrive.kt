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

/**
 * Data class representing a OneDrive object.
 *
 * @property value The list of files.
 */
data class OneDrive(
    val value: List<OneDriveFile>
)

/**
 * Data class representing a file from OneDrive.
 *
 * @property name The name of the file.
 * @property size The size of the file in bytes.
 * @property folder Is the file a folder.
 * @property file Is the file a file.
 * @property lastModifiedDateTime The timestamp of the last modification in ISO8601.
 * @property parentReference The parent reference of the file.
 */
data class OneDriveFile(
    val name: String,
    val size: Long,
    val folder: Any?,
    val file: Any?,
    val lastModifiedDateTime: String,
    val parentReference: ParentReference
)

/**
 * Data class representing the parent reference of a file from OneDrive.
 *
 * @property path The path of the parent reference.
 */
data class ParentReference(
    val path: String
)
