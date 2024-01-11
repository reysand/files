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
package com.reysand.files.data.repository

import com.reysand.files.data.model.AuthModel
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for interacting with authentication.
 */
interface AuthRepository {

    /**
     * Saves the user credentials.
     *
     * @param email The email of the user.
     */
    suspend fun saveAuth(email: String)

    /**
     * Removes the user credentials.
     */
    suspend fun removeAuth()

    /**
     * Gets the user credentials.
     *
     * @return [Flow] of [AuthModel] that emits the user credentials.
     */
    fun getAuth(): Flow<AuthModel?>
}