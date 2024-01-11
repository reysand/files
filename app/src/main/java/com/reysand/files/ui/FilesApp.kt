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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reysand.files.R
import com.reysand.files.ui.components.PathTabs
import com.reysand.files.ui.navigation.Destinations
import com.reysand.files.ui.navigation.NavGraph
import com.reysand.files.ui.util.OneDriveService
import com.reysand.files.ui.viewmodel.FilesViewModel

/**
 * Composable function representing the main UI of the files application.
 *
 * @param filesViewModel The [FilesViewModel] providing data for the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesApp(filesViewModel: FilesViewModel = viewModel(factory = FilesViewModel.Factory)) {
    // Create a navigation controller
    val navController = rememberNavController()

    val context = LocalContext.current

    // Get the current route from the navigation stack
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Determine the title for the top app bar based on the current route
    val topBarTitle = when (currentRoute) {
        Destinations.FILE_LIST -> R.string.internal_storage
        Destinations.SETTINGS -> R.string.settings_title
        else -> R.string.app_name
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(id = topBarTitle))
            },
            navigationIcon = {
                if (currentRoute != Destinations.HOME) {
                    IconButton(onClick = {
                        if (filesViewModel.currentDirectory.value != filesViewModel.homeDirectory &&
                            currentRoute == Destinations.FILE_LIST
                        ) {
                            filesViewModel.navigateUp()
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = null
                        )
                    }
                }
            },
            actions = {
                if (currentRoute == Destinations.HOME) {
                    IconButton(onClick = { navController.navigate(Destinations.SETTINGS) }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings, contentDescription = null
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F)
            )
        )
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                if (currentRoute == Destinations.FILE_LIST) {
                    PathTabs(
                        filesViewModel.homeDirectory, filesViewModel.currentDirectory.value
                    ) { newPath ->
                        filesViewModel.getFiles(newPath)
                    }
                }
                NavGraph(
                    filesViewModel = filesViewModel,
                    navController = navController,
                    oneDriveService = OneDriveService(context)
                )
            }
        }
    }
}