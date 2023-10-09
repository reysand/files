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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.reysand.files.R
import com.reysand.files.data.model.FileModel
import com.reysand.files.ui.theme.FilesTheme

/**
 * Composable function for displaying a single file item.
 *
 * @param file The [FileModel] representing the file.
 * @param homeDirectory The path to the home directory.
 * @param moveOperation Callback for when the move option is clicked.
 * @param copyOperation Callback for when the copy option is clicked.
 * @param renameOperation Callback for when the rename option is clicked.
 * @param deleteOperation Callback for when the delete option is clicked.
 * @param modifier Modifier for customizing the layout.
 * @param onClick Callback for when the item is clicked.
 */
@Composable
fun FileListItem(
    file: FileModel,
    homeDirectory: String,
    moveOperation: (FileModel, String) -> Unit,
    copyOperation: (FileModel, String) -> Unit,
    renameOperation: (FileModel, String) -> Unit,
    deleteOperation: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    // Determine the icon based on the file type
    val iconResId = when (file.fileType) {
        FileModel.FileType.DIRECTORY -> R.drawable.ic_folder
        FileModel.FileType.OTHER -> R.drawable.ic_file
    }

    // Determine the information text based on the file type
    val info = when (file.fileType) {
        FileModel.FileType.DIRECTORY -> file.getLastModified()
        else -> "${file.getFormattedSize()}, ${file.getLastModified()}"
    }

    ListItem(
        headlineContent = {
            Text(
                text = file.name,
                modifier = modifier,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        modifier = modifier.clickable(onClick = onClick),
        supportingContent = {
            Text(
                text = info,
                modifier = modifier
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = modifier
            )
        },
        trailingContent = {
            OptionsMenu(
                file = file,
                homeDirectory = homeDirectory,
                moveOperation = moveOperation,
                copyOperation = copyOperation,
                renameOperation = renameOperation,
                deleteOperation = deleteOperation
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
fun FileListItemPreview() {
    FilesTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                FileListItem(
                    file = FileModel(
                        name = "Android",
                        path = "/storage/emulated/0",
                        fileType = FileModel.FileType.DIRECTORY,
                        size = 1,
                        lastModified = 1_593_689_259_000
                    ),
                    homeDirectory = "",
                    moveOperation = { _, _ -> },
                    copyOperation = { _, _ -> },
                    renameOperation = { _, _ -> },
                    deleteOperation = {}
                ) {}
                FileListItem(
                    file = FileModel(
                        name = "text.txt",
                        path = "/storage/emulated/0",
                        fileType = FileModel.FileType.OTHER,
                        size = 1024,
                        lastModified = 1_693_689_259_000
                    ),
                    homeDirectory = "",
                    moveOperation = { _, _ -> },
                    copyOperation = { _, _ -> },
                    renameOperation = { _, _ -> },
                    deleteOperation = {}
                ) {}
            }
        }
    }
}