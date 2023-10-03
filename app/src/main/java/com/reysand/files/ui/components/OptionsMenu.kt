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
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.reysand.files.R
import com.reysand.files.data.model.FileModel
import com.reysand.files.ui.theme.FilesTheme
import com.reysand.files.ui.viewmodel.FilesViewModel

@Composable
fun OptionsMenu(
    file: FileModel,
    filesViewModel: FilesViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(0) }

    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            OptionsMenuItem(
                text = stringResource(id = R.string.options_move_to),
                leadingIcon = R.drawable.ic_option_move
            ) {
                showDialog = 1
                expanded = false
            }
            OptionsMenuItem(
                text = stringResource(id = R.string.options_copy_to),
                leadingIcon = R.drawable.ic_option_copy
            ) {
                showDialog = 2
                expanded = false
            }
            OptionsMenuItem(
                text = stringResource(id = R.string.options_rename),
                leadingIcon = R.drawable.ic_option_rename
            ) {
                showDialog = 3
                expanded = false
            }
            OptionsMenuItem(
                text = stringResource(id = R.string.options_delete),
                leadingIcon = R.drawable.ic_option_delete
            ) {
                showDialog = 4
                expanded = false
            }
        }
    }

    when (showDialog) {
        1 -> {
            RenameDialog(
                dialogTitle = stringResource(id = R.string.options_move_to),
                fileName = file.path.removePrefix(filesViewModel.homeDirectory),
                onConfirm = { newName ->
                    filesViewModel.moveFile(file, newName)
                    showDialog = 0
                },
                onDismiss = { showDialog = 0 }
            )
        }

        2 -> {
            RenameDialog(
                dialogTitle = stringResource(id = R.string.options_copy_to),
                fileName = file.path.removePrefix(filesViewModel.homeDirectory),
                onConfirm = { newName ->
                    filesViewModel.copyFile(file, newName)
                    showDialog = 0
                },
                onDismiss = { showDialog = 0 }
            )
        }

        3 -> {
            RenameDialog(
                dialogTitle = stringResource(id = R.string.options_rename),
                fileName = file.name,
                onConfirm = { newName ->
                    filesViewModel.renameFile(file, newName)
                    showDialog = 0
                },
                onDismiss = { showDialog = 0 }
            )
        }

        4 -> {
            DeleteDialog(
                fileName = file.name,
                onDelete = {
                    filesViewModel.deleteFile(file.path)
                    showDialog = 0
                },
                onDismiss = { showDialog = 0 }
            )
        }
    }
}

@Composable
fun OptionsMenuItem(
    text: String,
    @DrawableRes leadingIcon: Int,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = text) },
        onClick = onClick,
        leadingIcon = {
            Icon(painter = painterResource(id = leadingIcon), contentDescription = null)
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
fun OptionsMenuItemPreview() {
    FilesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            OptionsMenuItem(
                stringResource(id = R.string.options_rename),
                R.drawable.ic_option_rename
            ) {}
        }
    }
}