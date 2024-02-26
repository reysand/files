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
package com.reysand.files.data.remote

import android.util.Log
import com.reysand.files.data.repository.YandexUserRepository
import com.reysand.files.data.util.YandexUserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "YandexUserDataSource"

class YandexUserDataSource : YandexUserRepository {

    private val yandexService: YandexUserService by lazy {
        Retrofit.Builder()
            .baseUrl("https://login.yandex.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YandexUserService::class.java)
    }

    override suspend fun getInfo(oAuthToken: String) : String {
        val response = yandexService.getInfo("OAuth $oAuthToken")
        Log.d(TAG, "YandexUserService: $response")

        return response.body()?.email ?: ""
    }
}