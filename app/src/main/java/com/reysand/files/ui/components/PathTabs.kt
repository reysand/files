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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reysand.files.ui.theme.FilesTheme
import java.io.File

/**
 * Composable function for displaying a row of tabs representing the current directory path.
 *
 * @param homeDirectory The path to the home directory.
 * @param currentDirectory The path to the current directory.
 * @param onNavigateToDirectory Callback function for navigating to a specific directory.
 */
@Composable
fun PathTabs(
    homeDirectory: String,
    currentDirectory: String,
    onNavigateToDirectory: (String) -> Unit
) {
    // Extract the relative path from the home directory
    val path = currentDirectory.removePrefix(homeDirectory)

    // Split the path into individual components
    val pathComponents = listOf(homeDirectory) + path.split(File.separator).filter {
        it.isNotEmpty()
    }

    ScrollableTabRow(
        selectedTabIndex = pathComponents.size - 1,
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F),
        edgePadding = 0.dp
    ) {
        pathComponents.forEachIndexed { index, component ->
            Tab(selected = index == pathComponents.size - 1, onClick = {
                // Join the path components to form the new directory path
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

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PathTabsPreview() {
    FilesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            PathTabs(
                homeDirectory = "/storage/emulated/0",
                currentDirectory = "/storage/emulated/0${File.separator}Download"
            ) {}
        }
    }
}