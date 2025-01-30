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
package example.com.fielthyapps.data

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.await
import androidx.health.services.client.HealthServices
import androidx.health.services.client.MeasureCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DeltaDataType
import androidx.health.services.client.data.IntervalDataPoint
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.data.SampleDataPoint
import androidx.health.services.client.unregisterMeasureCallback
import example.com.fielthyapps.TAG
import example.com.fielthyapps.service.PassiveDataService
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.time.Instant
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.roundToInt

/**
 * Entry point for [HealthServicesClient] APIs. This also provides suspend functions around
 * those APIs to enable use in coroutines.
 */
class HealthServicesRepository(context: Context) {
    private val healthServicesClient = HealthServices.getClient(context)
    private val measureClient = healthServicesClient.measureClient
    private val dataClient by lazy { Wearable.getDataClient(context) }
    private val passiveMonitoringClient = healthServicesClient.passiveMonitoringClient
    private val dataTypes = setOf(DataType.HEART_RATE_BPM, DataType.STEPS_DAILY)

    private val passiveListenerConfig = PassiveListenerConfig(
        dataTypes = dataTypes,
        shouldUserActivityInfoBeRequested = true,
        dailyGoals = setOf(),
        healthEventTypes = setOf()
    )

    suspend fun hasHeartRateCapability(): Boolean {
        val capabilities = passiveMonitoringClient.getCapabilitiesAsync().await()
        val supportsHeartRate =
            DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
        // Supported types for PassiveGoals
        val supportsStepsGoal =
            DataType.STEPS_DAILY in capabilities.supportedDataTypesPassiveMonitoring
        val activeCapabilities = measureClient.getCapabilitiesAsync().await()
        val activeSupportsHeartRate = DataType.HEART_RATE_BPM in activeCapabilities.supportedDataTypesMeasure
        Log.d("TAG", "$activeSupportsHeartRate")
        return supportsHeartRate && supportsStepsGoal && activeSupportsHeartRate
    }

    suspend fun registerForHeartRateData() {
        Log.i(TAG, "Registering listener")
        passiveMonitoringClient.setPassiveListenerServiceAsync(
            PassiveDataService::class.java,
            passiveListenerConfig
        ).await()
    }

    suspend fun unregisterForHeartRateData() {
        Log.i(TAG, "Unregistering listeners")
        passiveMonitoringClient.clearPassiveListenerServiceAsync().await()
    }

    fun measureFlow(): Flow<MeasureMessage> = callbackFlow {

        val callback = object : MeasureCallback {
            override fun onAvailabilityChanged(dataType: DeltaDataType<*, *>, availability: Availability) {
                // Only send back DataTypeAvailability (not LocationAvailability)
                if (availability is DataTypeAvailability) {
                    trySendBlocking(MeasureMessage.MeasureAvailability(availability))
                }
            }

            override fun onDataReceived(data: DataPointContainer) {
                val heartRateBpm = data.getData(DataType.HEART_RATE_BPM)
//                Log.d(TAG, "💓 HR: ${heartRateBpm.first().value}")
                trySendBlocking(MeasureMessage.MeasureData(hrData = heartRateBpm, stepData = listOf()))
            }
        }

        Log.d(TAG, "⌛ Registering for data...")
        measureClient.registerMeasureCallback(DataType.HEART_RATE_BPM, callback)

        awaitClose {
            Log.d(TAG, "👋 Unregistering for data")
            runBlocking {
                measureClient.unregisterMeasureCallback(DataType.HEART_RATE_BPM, callback)
            }
        }
    }

    suspend fun sendHrToHandheldDevice(heartRate: Double) {
        try {
//            Log.d(TAG, "Send to handheld device ${Instant.now()}_${heartRate.roundToInt()}")

            val result = dataClient
                .putDataItem(
                    PutDataMapRequest
                        .create("/heartrate")
                        .apply { dataMap.putString("heartrate", "${Instant.now()}_${heartRate.roundToInt()}") }
                        .asPutDataRequest()
                        .setUrgent())
                .await()

//            Log.d("TAG", "DataItem saved: $result")
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.d("TAG", "Saving DataItem failed: $exception")
        }
    }

    suspend fun sendStepToHandheldDevice(step: Long) {
        try {
            Log.d(TAG, "Send to handheld device ${Instant.now()}_${step}")

            val result = dataClient
                .putDataItem(
                    PutDataMapRequest
                        .create("/steps")
                        .apply { dataMap.putString("steps", "${Instant.now()}_${step}") }
                        .asPutDataRequest()
                        .setUrgent())
                .await()

            Log.d("TAG", "DataItem saved: $result")
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (exception: Exception) {
            Log.d("TAG", "Saving DataItem failed: $exception")
        }
    }
}

sealed class MeasureMessage {
    class MeasureAvailability(val availability: DataTypeAvailability) : MeasureMessage()
    class MeasureData(
        val stepData: List<IntervalDataPoint<Long>>,
        val hrData: List<SampleDataPoint<Double>>
    ) : MeasureMessage()
}