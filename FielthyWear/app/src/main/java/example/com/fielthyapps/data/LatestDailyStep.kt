package example.com.fielthyapps.data

import android.util.Log
import androidx.health.services.client.data.IntervalDataPoint

fun  List<IntervalDataPoint<Long>>.latestDailyStep(): Long? {
    return this.firstOrNull()?.value
}