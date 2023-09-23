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
import androidx.compose.material3.ExperimentalMaterial3Api
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
 * @param modifier Modifier for customizing the layout.
 * @param onClick Callback for when the item is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileListItem(file: FileModel, modifier: Modifier = Modifier, onClick: () -> Unit) {

    // Determine the icon based on the file type
    val icon = when (file.fileType) {
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
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = modifier
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
                    FileModel(
                        name = "Android",
                        path = "",
                        fileType = FileModel.FileType.DIRECTORY,
                        size = 1,
                        lastModified = 1_593_689_259_000
                    ),
                    onClick = {}
                )
                FileListItem(
                    FileModel(
                        name = "text.txt",
                        path = "",
                        fileType = FileModel.FileType.OTHER,
                        size = 1024,
                        lastModified = 1_693_689_259_000
                    ),
                    onClick = {}
                )
            }
        }
    }
}