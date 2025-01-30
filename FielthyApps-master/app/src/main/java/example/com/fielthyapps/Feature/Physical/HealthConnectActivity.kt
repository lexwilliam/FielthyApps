package example.com.fielthyapps.Feature.Physical

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import example.com.fielthyapps.R
import example.com.fielthyapps.Service.DataLayerListenerService
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


class HealthConnectActivity : AppCompatActivity() {
    private lateinit var healthConnectClient: HealthConnectClient
    private lateinit var tvStep: TextView
    private lateinit var tvHeartBeat: TextView
    private lateinit var tvActiveCalories: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_health_connect)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startService(Intent(this, DataLayerListenerService::class.java))
        initUI()
        initHealthConnect()
    }

    private fun initUI() {
        tvStep = findViewById(R.id.tvStep)
        tvHeartBeat = findViewById(R.id.tvHeartBeat)
        tvActiveCalories = findViewById(R.id.tvActiveCalories)
    }

    private fun initHealthConnect() {
        healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
        val permissions =
            setOf(
                HealthPermission.getReadPermission(HeartRateRecord::class),
                HealthPermission.getWritePermission(HeartRateRecord::class),
                HealthPermission.getReadPermission(StepsRecord::class),
                HealthPermission.getWritePermission(StepsRecord::class),
                HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
            )

        // Create the permissions launcher
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

        val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(permissions)) {
                // Permissions successfully granted
                initHealthConnectClient()
            } else {
                // Lack of required permissions
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }

        suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (granted.containsAll(permissions)) {
                // Permissions already granted; proceed with inserting or reading data
                initHealthConnectClient()
            } else {
                requestPermissions.launch(permissions)
            }
        }

        lifecycleScope.launch {
            checkPermissionsAndRun(healthConnectClient)
        }
    }

    private fun initHealthConnectClient() {
        val providerPackageName = "com.google.android.apps.healthdata"
        val availabilityStatus = HealthConnectClient.getSdkStatus(applicationContext, providerPackageName)
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return // early return as there is no viable integration
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            // Optionally redirect to package installer to find a provider, for example:
            val uriString = "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
            this.startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("example.com.fielthyapps")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", applicationContext.packageName)
                }
            )
            return
        }
        lifecycleScope.launch {
            val startTime = Instant.now().truncatedTo(java.time.temporal.ChronoUnit.DAYS).atZone(java.time.ZoneOffset.UTC).toInstant()
            val endTime = Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            Log.d("TAG", "startTime: $startTime, endTime: $endTime")
            readHealthConnectRecord(healthConnectClient, startTime, endTime)
        }
    }

    private suspend fun readHealthConnectRecord(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant
    ) {
        try {
            val stepResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                ),
            )
            val stepRecord = stepResponse.records.maxOfOrNull { it.count } ?: 0
            tvStep.text = stepRecord.toString()
            tvActiveCalories.text = (stepRecord * 0.05f).toString()
            val heartBeatResponse = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    HeartRateRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                ),
            )
            if (heartBeatResponse.records.isEmpty()) {
                tvHeartBeat.text = "Not Found"
            }
            val heartBeatRecord = heartBeatResponse.records.firstOrNull()
            tvHeartBeat.text = heartBeatRecord?.samples?.firstOrNull()?.beatsPerMinute?.toString() ?: "Not Found"
        } catch (e: Exception) {
            // Run error handling here
        }
    }
}