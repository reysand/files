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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.reysand.files.ui.navigation.Destinations
import com.reysand.files.ui.viewmodel.FilesViewModel

/**
 * Composable function for the Home screen.
 *
 * @param filesViewModel The [FilesViewModel] providing data for the screen.
 * @param navController NavHostController for managing navigation within the app.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun HomeScreen(
    filesViewModel: FilesViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navController.navigate(Destinations.FILE_LIST)
}