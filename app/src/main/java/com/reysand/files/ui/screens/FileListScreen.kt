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

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.reysand.files.data.model.FileModel
import com.reysand.files.ui.components.FileListItem
import com.reysand.files.ui.components.PermissionAlertDialog
import com.reysand.files.ui.viewmodel.FilesViewModel

/**
 * Composable function for displaying a list of files.
 *
 * @param filesViewModel The [FilesViewModel] providing data for the screen.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun FileListScreen(
    filesViewModel: FilesViewModel,
    modifier: Modifier = Modifier
) {
    // Collect the list of files as state
    val files by filesViewModel.files.collectAsState(initial = emptyList())

    // Display permission alert dialog if needed
    PermissionAlertDialog(showPermissionDialog = filesViewModel.showPermissionDialog)

    LazyColumn(modifier = modifier) {
        items(files) { file ->
            FileListItem(file = file) {
                if (file.fileType == FileModel.FileType.DIRECTORY) {
                    filesViewModel.getFiles(file.path)
                }
            }
        }
    }
}