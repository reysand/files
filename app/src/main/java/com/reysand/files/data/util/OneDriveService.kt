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

import com.reysand.files.data.model.OneDrive
import com.reysand.files.data.model.OneDriveQuota
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OneDriveService {

    @GET("me/drive")
    suspend fun getDrive(@Header("Authorization") accessToken: String): Response<OneDriveQuota>

    @GET("me/drive/root:/{path}:/children")
    suspend fun getFiles(
        @Header("Authorization") accessToken: String,
        @Path("path") path: String
    ): Response<OneDrive>
}
