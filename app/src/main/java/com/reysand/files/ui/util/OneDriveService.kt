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
package com.reysand.files.ui.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.SignInParameters
import com.microsoft.identity.client.exception.MsalException
import com.reysand.files.R
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

private const val TAG = "OneDriveService"

/**
 * Service class for accessing OneDrive.
 *
 * @param context The context of the app.
 */
class OneDriveService(val context: Context) {

    var mAccount: IAccount? = null
    private val scopes: List<String> = listOf("User.Read")

    /**
     * Gets the [ISingleAccountPublicClientApplication] instance.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private val msalPublicClient: Deferred<ISingleAccountPublicClientApplication> by lazy {
        GlobalScope.async(Dispatchers.IO) {
            PublicClientApplication.createSingleAccountPublicClientApplication(
                context,
                R.raw.auth_config_single_account
            )
        }
    }

    /**
     * Signs in the user.
     *
     * @param callback The callback to be invoked when the sign in process finishes.
     */
    suspend fun signIn(callback: (String?) -> Unit) {
        val signInParameters = SignInParameters.builder()
            .withActivity(context as Activity)
            .withScopes(scopes)
            .withCallback(object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    mAccount = authenticationResult?.account
                    callback(mAccount?.username)
                    Log.d(TAG, "signIn: Success")
                }

                override fun onError(exception: MsalException?) {
                    exception?.printStackTrace()
                }

                override fun onCancel() {
                    Log.d(TAG, "signIn: Cancelled")
                }
            })
            .build()

        val client = msalPublicClient.await()
        client.signIn(signInParameters)
    }

    /**
     * Checks whether the user is signed in.
     *
     * @return A boolean value indicating whether the user is signed in.
     */
    suspend fun isSignedIn(): Boolean = withContext(Dispatchers.IO) {
        try {
            val client = msalPublicClient.await()
            val account = client.currentAccount
            account.currentAccount != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Signs out the current account.
     */
    suspend fun signOut() = withContext(Dispatchers.IO) {
        val client = msalPublicClient.await()
        client.signOut()
        Log.d(TAG, "signOut: Success")
    }
}