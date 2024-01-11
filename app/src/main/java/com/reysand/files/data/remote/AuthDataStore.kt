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
package com.reysand.files.data.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.reysand.files.data.model.AuthModel
import com.reysand.files.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_NAME = "user_preferences"

val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

/**
 * Implementation of [AuthRepository] using DataStore for storing user credentials.
 *
 * @param context The application context.
 */
class AuthDataStore(context: Context) : AuthRepository {

    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val EMAIL = stringPreferencesKey("email")
    }

    override suspend fun saveAuth(email: String) {
        val authInfo = AuthModel(email)
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EMAIL] = authInfo.email
        }
    }

    override suspend fun removeAuth() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.EMAIL)
        }
    }

    override fun getAuth(): Flow<AuthModel?> {
        return dataStore.data.map { preferences ->
            val email = preferences[PreferencesKeys.EMAIL] ?: ""
            if (email.isNotEmpty()) {
                AuthModel(email)
            } else {
                null
            }
        }
    }
}
