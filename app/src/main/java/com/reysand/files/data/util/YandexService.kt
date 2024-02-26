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

import com.reysand.files.data.model.YandexUser
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthToken
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface YandexUserService {

    @GET("info?format=json")
    suspend fun getInfo(@Header("Authorization") oAuthToken: String): Response<YandexUser>
}

/**
 * Service for accessing Yandex API.
 */
class YandexService {

    var token: YandexAuthToken? = null

    fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> token = result.token
//            is YandexAuthResult.Failure -> onProccessError(result.exception)
//            YandexAuthResult.Cancelled -> onCancelled()
            else -> {}
        }
    }
}