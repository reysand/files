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

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.identity.client.AcquireTokenSilentParameters
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

private const val TAG = "MicrosoftService"

/**
 * Service class for accessing OneDrive.
 *
 * @param context The context of the app.
 */
@OptIn(DelicateCoroutinesApi::class)
class MicrosoftService(val context: Context) {

    var mAccount: IAccount? = null
    var mAccessToken: String? = null
    private val scopes: List<String> = listOf("User.Read")

    val usernameFlow: Flow<String?> = flow {
        emit(mAccount?.username)
    }

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
     * @param context The context of the app.
     * @param callback The callback to be invoked when the sign in process finishes.
     */
    suspend fun signIn(context: Context, callback: (String?) -> Unit) {
        val signInParameters = SignInParameters.builder()
            .withActivity(context as Activity)
            .withScopes(scopes)
            .withCallback(object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    mAccount = authenticationResult?.account
                    mAccessToken = authenticationResult?.accessToken
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
     * Acquires an access token silently.
     */
    suspend fun acquireTokenSilently() = withContext(Dispatchers.IO) {
        val client = msalPublicClient.await()
        mAccount = client.currentAccount.currentAccount

        val silentParameters: AcquireTokenSilentParameters = AcquireTokenSilentParameters.Builder()
            .forAccount(mAccount)
            .fromAuthority(mAccount?.authority)
            .withScopes(scopes)
            .withCallback(object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    mAccessToken = authenticationResult?.accessToken
                    Log.d(TAG, "acquireTokenSilently: Success")
                }

                override fun onError(exception: MsalException?) {
                    exception?.printStackTrace()
                }

                override fun onCancel() {
                    Log.d(TAG, "acquireTokenSilently: Cancelled")
                }
            })
            .build()

        client.acquireTokenSilentAsync(silentParameters)
    }

    /**
     * Checks whether the user is signed in.
     *
     * @return A boolean value indicating whether the user is signed in.
     */
    suspend fun isSignedIn(): Boolean = withContext(Dispatchers.IO) {
        try {
            val client = msalPublicClient.await()
            client.currentAccount.currentAccount != null
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
        mAccount = null
        mAccessToken = null
        Log.d(TAG, "signOut: Success")
    }
}