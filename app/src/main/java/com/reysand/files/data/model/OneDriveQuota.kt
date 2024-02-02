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
 * Data class representing the quota of a OneDrive account.
 *
 * @property quota The quota of the OneDrive account.
 */
data class OneDriveQuota(
    val quota: Quota
)

/**
 * Data class representing the quota details.
 *
 * @property remaining The remaining quota in bytes.
 */
data class Quota(
    val remaining: Long
)
