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
package com.reysand.files.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.reysand.files.R
import com.reysand.files.ui.components.allowPermission
import com.reysand.files.ui.theme.FilesTheme
import com.reysand.files.ui.viewmodel.FilesViewModel
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable function for displaying the settings screen.
 *
 * @param filesViewModel The [FilesViewModel] providing data for the screen.
 */
@Composable
fun SettingsScreen(filesViewModel: FilesViewModel) {

    val context = LocalContext.current
    val oneDriveAccount by remember { filesViewModel.oneDriveAccount }
    val yandexDiskAccount by remember { filesViewModel.yandexDiskAccount }

    val sdk = YandexAuthSdk.create(YandexAuthOptions(context))
    val launcher = rememberLauncherForActivityResult(sdk.contract) { result ->
        filesViewModel.handleResult(result)
    }
    val loginOptions = YandexAuthLoginOptions()

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        SettingsSection(title = stringResource(id = R.string.settings_account_title)) {
            val scope = rememberCoroutineScope()

            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.onedrive_storage))
                },
                modifier = Modifier.clickable(onClick = {
                    scope.launch {
                        filesViewModel.toggleMicrosoftSignIn(context)
                    }
                }),
                trailingContent = {
                    Text(
                        text = oneDriveAccount
                            ?: stringResource(id = R.string.settings_account_sign_in),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = Dp.Hairline
            )

            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.yandex_disk_storage))
                },
                modifier = Modifier.clickable(onClick = {
                    scope.launch {
                        launcher.launch(loginOptions)
                        delay(2000)
                        filesViewModel.getInfo()
                    }
                }),
                trailingContent = {
                    Text(
                        text = yandexDiskAccount
                            ?: stringResource(id = R.string.settings_account_sign_in),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
        SettingsSection(title = stringResource(id = R.string.settings_privacy_title)) {
            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.file_access_permission_title))
                },
                modifier = Modifier.clickable(onClick = { allowPermission(context) }),
                trailingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launch),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

/**
 * Composable function for a settings section with a title.
 *
 * @param title The title of the settings section.
 * @param modifier [Modifier] for customizing the layout.
 * @param content Content of the settings section.
 */
@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Text(
        text = title,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(content = content)
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun SettingsSectionPreview() {
    FilesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                SettingsSection(title = stringResource(id = R.string.settings_privacy_title)) {
                    ListItem(
                        headlineContent = {
                            Text(text = stringResource(id = R.string.file_access_permission_title))
                        },
                        trailingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_launch),
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}