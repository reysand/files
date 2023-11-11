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
package com.reysand.files.ui.components

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.reysand.files.R
import com.reysand.files.ui.theme.FilesTheme

/**
 * Composable function to display a permission alert dialog.
 *
 * @param showPermissionDialog State that controls whether the dialog is shown.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun PermissionAlertDialog(
    showPermissionDialog: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (showPermissionDialog.value) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    allowPermission(context)
                    showPermissionDialog.value = false
                }) {
                    Text(text = stringResource(id = R.string.file_access_permission_confirm_button))
                }
            },
            modifier = modifier,
            dismissButton = {
                TextButton(onClick = { showPermissionDialog.value = false }) {
                    Text(text = stringResource(id = R.string.dialog_dismiss_button))
                }
            },
            icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
            title = { Text(text = stringResource(id = R.string.file_access_permission_title)) },
            text = { Text(text = stringResource(id = R.string.file_access_permission_message)) },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}

/**
 * Utility function to open the system settings for app permission management.
 *
 * @param context The context of the current application.
 */
fun allowPermission(context: Context) {
    val uri = Uri.parse("package:${context.packageName}")
    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
    context.startActivity(intent)
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PermissionAlertDialogPreview() {
    FilesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            PermissionAlertDialog(remember { mutableStateOf(true) })
        }
    }
}