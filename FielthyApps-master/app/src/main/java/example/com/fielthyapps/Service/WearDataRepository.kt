package example.com.fielthyapps.Service

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import java.time.Instant
import java.time.ZoneOffset

class WearDataRepository private constructor(context: Context) {

//    val healthConnectClient = HealthConnectClient.getOrCreate(context)

    fun saveHeartRate(heartRate: Int) {
        val hrRecord = HeartRateRecord(
            startTime = Instant.now(),
            endTime = Instant.now(),
            startZoneOffset = ZoneOffset.UTC,
            endZoneOffset = ZoneOffset.UTC,
            samples = listOf(
                HeartRateRecord.Sample(
                    time = Instant.now(), beatsPerMinute = heartRate.toLong(),
                )
            )
        )
//        healthConnectClient.insertRecords(listOf(hrRecord))
    }

    fun saveSteps(dailySteps: Int) {
        try {
            val stepsRecord = StepsRecord(
                count = dailySteps.toLong(),
                startTime = Instant.now(),
                endTime = Instant.now(),
                startZoneOffset = ZoneOffset.UTC,
                endZoneOffset = ZoneOffset.UTC
            )
//            healthConnectClient.insertRecords(listOf(stepsRecord))
        } catch (e: Exception) {
            // Run error handling here
            e.printStackTrace()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WearDataRepository? = null

        fun getInstance(context: Context): WearDataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: WearDataRepository(context).also { INSTANCE = it }
            }
    }
}