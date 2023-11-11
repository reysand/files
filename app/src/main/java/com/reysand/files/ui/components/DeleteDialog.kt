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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.reysand.files.R
import com.reysand.files.ui.theme.FilesTheme

/**
 * Composable function for displaying a delete dialog.
 *
 * @param fileName The name of the file to be deleted.
 * @param onDelete Callback for when the delete button is clicked.
 * @param onDismiss Callback for when the dialog is dismissed.
 */
@Composable
fun DeleteDialog(
    fileName: String,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            DialogOutlinedButton(
                text = stringResource(id = R.string.dialog_confirm_button),
                icon = Icons.Default.Done,
                onClick = { onDelete() }
            )
        },
        dismissButton = {
            DialogOutlinedButton(
                text = stringResource(id = R.string.dialog_dismiss_button),
                icon = Icons.Default.Close,
                onClick = { onDismiss() }
            )
        },
        title = { Text(text = stringResource(id = R.string.dialog_delete_title)) },
        text = {
            Text(text = stringResource(id = R.string.dialog_delete_message, fileName))
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
fun DeleteDialogPreview() {
    FilesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DeleteDialog(
                fileName = "test.txt",
                onDelete = { },
                onDismiss = { }
            )
        }
    }
}