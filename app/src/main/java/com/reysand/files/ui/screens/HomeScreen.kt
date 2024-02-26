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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.reysand.files.R
import com.reysand.files.ui.components.StorageCard
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
    val oneDriveAccount by remember { filesViewModel.oneDriveAccount }
    val oneDriveStorageFreeSpace = remember { mutableStateOf("Not signed in") }

    val yandexDiskAccount by remember { filesViewModel.yandexDiskAccount }
    val yandexDiskStorageFreeSpace = remember { mutableStateOf("Not signed in") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        StorageCard(
            title = stringResource(id = R.string.internal_storage),
            leadingIcon = R.drawable.ic_internal_storage,
            info = filesViewModel.getStorageFreeSpace()
        ) {
            filesViewModel.setCurrentStorage("Local")
            navController.navigate(Destinations.FILE_LIST)
        }

        LaunchedEffect(oneDriveAccount) {
            oneDriveAccount?.let {
                oneDriveStorageFreeSpace.value = filesViewModel.getOneDriveStorageFreeSpace()
            }
        }

        StorageCard(
            title = stringResource(id = R.string.onedrive_storage),
            enabled = oneDriveAccount != null,
            leadingIcon = R.drawable.ic_cloud_storage,
            info = oneDriveStorageFreeSpace.value
        ) {
            filesViewModel.setCurrentStorage("OneDrive")
            navController.navigate(Destinations.FILE_LIST)
        }

        LaunchedEffect(yandexDiskAccount) {
            yandexDiskAccount?.let {
                yandexDiskStorageFreeSpace.value = filesViewModel.getYandexDiskStorageFreeSpace()
            }
        }

        StorageCard(
            title = stringResource(id = R.string.yandex_disk_storage),
            enabled = yandexDiskAccount != null,
            leadingIcon = R.drawable.ic_cloud_storage,
            info = yandexDiskStorageFreeSpace.value
        ) {
            filesViewModel.setCurrentStorage("YandexDisk")
            navController.navigate(Destinations.FILE_LIST)
        }
    }
}