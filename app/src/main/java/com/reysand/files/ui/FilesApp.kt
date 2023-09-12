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
package com.reysand.files.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reysand.files.R
import com.reysand.files.ui.screens.HomeScreen
import com.reysand.files.ui.viewmodel.FilesViewModel

/**
 * Composable function representing the main UI of the files application.
 *
 * @param modifier Modifier for customizing the layout.
 * @param filesViewModel ViewModel for managing file-related data and operations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesApp(
    modifier: Modifier = Modifier,
    filesViewModel: FilesViewModel = viewModel(factory = FilesViewModel.Factory)
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = modifier
                    )
                },
                modifier = modifier,
                navigationIcon = {
                    // Display back arrow if not in the home directory
                    if (filesViewModel.currentDirectory.value != filesViewModel.homeDirectory) {
                        IconButton(onClick = { filesViewModel.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F)
                ),
            )
        }
    ) {
        HomeScreen(
            filesViewModel = filesViewModel,
            modifier = modifier.padding(it)
        )
    }
}