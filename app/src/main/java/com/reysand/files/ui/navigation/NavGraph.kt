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
package com.reysand.files.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.reysand.files.ui.screens.FileListScreen
import com.reysand.files.ui.screens.HomeScreen
import com.reysand.files.ui.screens.SettingsScreen
import com.reysand.files.ui.util.OneDriveService
import com.reysand.files.ui.viewmodel.FilesViewModel

/**
 * Composable function for setting up the navigation graph.
 *
 * @param filesViewModel The [FilesViewModel] providing data for the screen.
 * @param navController NavHostController for managing navigation within the app.
 * @param oneDriveService The [OneDriveService] for accessing OneDrive.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun NavGraph(
    filesViewModel: FilesViewModel,
    navController: NavHostController,
    oneDriveService: OneDriveService,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Destinations.HOME) {
        composable(Destinations.HOME) {
            HomeScreen(filesViewModel = filesViewModel, navController = navController, modifier = modifier)
        }
        composable(Destinations.FILE_LIST) {
            FileListScreen(filesViewModel = filesViewModel)
        }
        composable(Destinations.SETTINGS) {
            SettingsScreen(filesViewModel = filesViewModel, oneDriveService = oneDriveService)
        }
    }
}