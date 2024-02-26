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

import com.reysand.files.data.model.RenameRequest
import com.reysand.files.data.model.YandexDisk
import com.reysand.files.data.model.YandexDiskQuota
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface YandexDiskService {

    @GET("disk")
    suspend fun getDisk(@Header("Authorization") oAuthToken: String): Response<YandexDiskQuota>

    @GET("disk/resources")
    suspend fun getFiles(
        @Header("Authorization") accessToken: String,
        @Query("path") path: String
    ): Response<YandexDisk>

    @POST("disk/resources/move")
    suspend fun moveFile(
        @Header("Authorization") accessToken: String,
        @Query("from") from: String,
        @Query("path") path: String
    ): Response<Unit>

    @POST("disk/resources/copy")
    suspend fun copyFile(
        @Header("Authorization") accessToken: String,
        @Query("from") from: String,
        @Query("path") path: String
    ): Response<Unit>

    @PATCH("disk/resources")
    suspend fun renameFile(
        @Header("Authorization") accessToken: String,
        @Query("path") path: String,
        @Body renameRequest: RenameRequest
    ): Response<Unit>

    @DELETE("disk/resources")
    suspend fun deleteFile(
        @Header("Authorization") accessToken: String,
        @Query("path") path: String
    ): Response<Unit>
}