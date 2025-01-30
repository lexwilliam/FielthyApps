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
package example.com.fielthyapps.service

import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import example.com.fielthyapps.data.HealthServicesRepository
import example.com.fielthyapps.data.PassiveDataRepository
import example.com.fielthyapps.data.latestDailyStep
import example.com.fielthyapps.data.latestHeartRate
import kotlinx.coroutines.runBlocking

/**
 * Service to receive data from Health Services.
 *
 * Passive data is delivered from Health Services to this service. Override the appropriate methods
 * in [PassiveListenerService] to receive updates for new data points, goals achieved etc.
 */
class PassiveDataService : PassiveListenerService() {
    private lateinit var repository: PassiveDataRepository
    private lateinit var healthServicesRepository: HealthServicesRepository

    override fun onCreate() {
        super.onCreate()
        repository = PassiveDataRepository(this)
        healthServicesRepository = HealthServicesRepository(this)
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        Log.d("TAGS", "Received new data points")
        runBlocking {
            dataPoints.getData(DataType.HEART_RATE_BPM).latestHeartRate()?.let {
                Log.d("TAGS", "HR: $it")
                repository.storeLatestHeartRate(it)
                healthServicesRepository.sendHrToHandheldDevice(it)
            }
            dataPoints.getData(DataType.STEPS_DAILY).latestDailyStep()?.let {
                Log.d("TAGS", "STEPS: $it")
                repository.storeDailyStep(it.toDouble())
                healthServicesRepository.sendStepToHandheldDevice(it)
            }
        }
    }
}
