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

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.reysand.files.ui.viewmodel.FilesViewModel
import java.io.File

/**
 * Composable function for displaying a row of tabs representing the current directory path.
 *
 * @param filesViewModel The [FilesViewModel] providing data for the screen.
 * @param onNavigateToDirectory Callback function for navigating to a specific directory.
 */
@Composable
fun PathTabs(filesViewModel: FilesViewModel, onNavigateToDirectory: (String) -> Unit) {
    val path = filesViewModel.currentDirectory.value.removePrefix(filesViewModel.homeDirectory)
    val pathComponents = listOf(filesViewModel.homeDirectory) +
            path.split(File.separator).filter { it.isNotEmpty() }

    ScrollableTabRow(
        selectedTabIndex = pathComponents.size - 1,
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F),
        edgePadding = 0.dp
    ) {
        pathComponents.forEachIndexed { index, component ->
            Tab(selected = index == pathComponents.size - 1, onClick = {
                val newPath = pathComponents.subList(0, index + 1).joinToString(File.separator)
                onNavigateToDirectory(newPath)
            }, text = {
                Text(
                    text = component,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    maxLines = 1
                )
            })
        }
    }
}