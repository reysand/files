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

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.reysand.files.R
import com.reysand.files.ui.theme.FilesTheme

/**
 * Composable function for displaying a rename dialog.
 *
 * @param dialogTitle The title of the dialog.
 * @param fileName The name of the file to be renamed.
 * @param onConfirm The callback to be invoked when the confirm button is clicked.
 * @param onDismiss The callback to be invoked when the dialog is dismissed.
 */
@Composable
fun RenameDialog(
    dialogTitle: String,
    fileName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf(fileName) }
    var confirmButtonEnabled by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            DialogOutlinedButton(
                icon = Icons.Default.Done,
                text = stringResource(id = R.string.dialog_confirm_button),
                enabled = confirmButtonEnabled,
                onClick = { onConfirm(newName) }
            )
        },
        dismissButton = {
            DialogOutlinedButton(
                icon = Icons.Default.Close,
                text = stringResource(id = R.string.dialog_dismiss_button),
                onClick = onDismiss
            )
        },
        title = { Text(text = dialogTitle) },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = {
                    newName = it
                    confirmButtonEnabled = (newName != fileName) && newName.isNotBlank()
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onConfirm(newName) }),
                singleLine = true,
                maxLines = 1
            )
        }
    )
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun RenameDialogPreview() {
    FilesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            RenameDialog("Test", "test.txt", {}, {})
        }
    }
}