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
 * Data class representing the quota of a Yandex.Disk account.
 *
 * @property total The total quota of the Yandex.Disk account.
 * @property used The used quota of the Yandex.Disk account.
 */
data class YandexDiskQuota(
    @SerializedName("total_space")
    val total: Long,
    @SerializedName("used_space")
    val used: Long
)
