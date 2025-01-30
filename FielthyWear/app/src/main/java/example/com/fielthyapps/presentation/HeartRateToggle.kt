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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import example.com.fielthyapps.R
import example.com.fielthyapps.theme.PassiveDataTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import example.com.fielthyapps.PERMISSIONS

/**
 * A [ToggleChip] for enabling / disabling passive monitoring.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HeartRateToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    permissionState: MultiplePermissionsState,
    modifier: Modifier = Modifier
) {
    ToggleChip(
        modifier = modifier,
        checked = checked,
        colors = ToggleChipDefaults.toggleChipColors(),
        onCheckedChange = { enabled ->
            if (permissionState.allPermissionsGranted) {
                onCheckedChange(enabled)
            } else {
                permissionState.launchMultiplePermissionRequest()
            }
        },
        label = { Text("Recording") },
        toggleControl = {
            Icon(
                imageVector = ToggleChipDefaults.switchIcon(checked),
                contentDescription = stringResource(id = R.string.heart_rate_toggle)
            )
        }
    )
}

//@OptIn(ExperimentalPermissionsApi::class)
//@Preview(
//    device = Devices.WEAR_OS_SMALL_ROUND,
//    showSystemUi = true
//)
//@Composable
//fun HeartRateTogglePreview() {
//    val permissionState = object : PermissionState {
//        override val permission = PERMISSIONS
//        override val status: PermissionStatus = PermissionStatus.Granted
//        override fun launchPermissionRequest() {}
//    }
//    PassiveDataTheme {
//        HeartRateToggle(
//            checked = true,
//            onCheckedChange = {},
//            permissionState = permissionState
//        )
//    }
//}
