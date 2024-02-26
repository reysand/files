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
package com.reysand.files.data

import android.content.Context
import com.reysand.files.data.local.FileLocalDataSource
import com.reysand.files.data.remote.FileOneDriveDataSource
import com.reysand.files.data.remote.YandexUserDataSource
import com.reysand.files.data.repository.FileRepository
import com.reysand.files.data.repository.OneDriveRepository
import com.reysand.files.data.repository.YandexUserRepository
import com.reysand.files.data.util.MicrosoftService
import com.reysand.files.data.util.YandexService

interface AppContainer {

    val fileRepository: FileRepository

    val oneDriveRepository: OneDriveRepository

    val yandexUserRepository: YandexUserRepository

    val microsoftService: MicrosoftService

    val yandexService: YandexService
}

class DefaultAppContainer(context: Context) : AppContainer {

    override val fileRepository: FileRepository by lazy {
        FileLocalDataSource()
    }

    override val oneDriveRepository: OneDriveRepository by lazy {
        FileOneDriveDataSource(microsoftService)
    }

    override val yandexUserRepository: YandexUserRepository by lazy {
        YandexUserDataSource()
    }

    override val microsoftService: MicrosoftService by lazy {
        MicrosoftService(context)
    }

    override val yandexService: YandexService by lazy {
        YandexService()
    }
}