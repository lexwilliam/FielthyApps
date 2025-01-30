/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.com.fielthyapps.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import example.com.fielthyapps.data.HealthServicesRepository
import example.com.fielthyapps.data.PassiveDataRepository
import example.com.fielthyapps.theme.PassiveDataTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import example.com.fielthyapps.PERMISSIONS
import example.com.fielthyapps.data.MeasureMessage

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PassiveDataApp(
    healthServicesRepository: HealthServicesRepository,
    passiveDataRepository: PassiveDataRepository
) {
    PassiveDataTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            timeText = { TimeText() }
        ) {
            val viewModel: PassiveDataViewModel = viewModel(
                factory = PassiveDataViewModelFactory(
                    healthServicesRepository = healthServicesRepository,
                    passiveDataRepository = passiveDataRepository
                )
            )
            val measureValue by viewModel.measureValue.collectAsState()
            val stepsValue by viewModel.stepsValue.collectAsState()
            val recordingEnabled by viewModel.recordingEnabled.collectAsState()
            val uiState by viewModel.uiState

            if (uiState == UiState.Supported) {
                val permissionState = rememberMultiplePermissionsState(
                    permissions = PERMISSIONS,
                    onPermissionsResult = { granted ->
                        if (granted.values.all { true }) {
                            viewModel.toggleEnabled()
                        }
                    }
                )
               when (val value = measureValue) {
                   is MeasureMessage.MeasureAvailability -> {}
                   is MeasureMessage.MeasureData -> {
                       PassiveDataScreen(
                           hrValue = value.hrData,
                           stepValue = stepsValue,
                           recordingEnabled = recordingEnabled,
                           onEnableClick = { viewModel.toggleEnabled() },
                           permissionState = permissionState
                       )
                   }
               }
            } else if (uiState == UiState.NotSupported) {
                NotSupportedScreen()
            }
        }
    }
}
