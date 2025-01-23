package example.com.fielthyapps.Service

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService

class DataLayerListenerService: WearableListenerService() {

    private val repository = WearDataRepository.getInstance(context = this)

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("TAG", "onDataChanged")

        dataEvents.forEach { event ->
            when (event.type) {
                DataEvent.TYPE_CHANGED -> {
                    event.dataItem.run {
                        if (uri.path?.compareTo("/heartrate") == 0) {
                            val heartRate = DataMapItem.fromDataItem(this)
                                .dataMap.getInt("heartrate")
                            Log.d("DataLayerListenerService",
                                "New heart rate value received: $heartRate")
                            repository.saveHeartRate(heartRate)
                        } else if (uri.path?.compareTo("/dailysteps") == 0) {
                            val dailySteps = DataMapItem.fromDataItem(this)
                                .dataMap.getInt("dailysteps")
                            Log.d("DataLayerListenerService",
                                "New daily steps value received: $dailySteps")
                            repository.saveSteps(dailySteps)
                        }
                    }
                }

                DataEvent.TYPE_DELETED -> {
                    // DataItem deleted
                }
            }
        }
    }
}