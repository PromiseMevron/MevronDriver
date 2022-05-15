package com.mevron.rides.driver.location

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.CallSuper
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import com.mevron.rides.driver.location.domain.model.LocationData
import com.mevron.rides.driver.location.domain.repository.ILocationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "LUBroadcastReceiver"

abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}

@AndroidEntryPoint
class LocationUpdatesBroadcastReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var repository: ILocationRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive() context:$context, intent:$intent")

        if (intent.action == ACTION_PROCESS_UPDATES) {

            // Checks for location availability changes.
            LocationAvailability.extractLocationAvailability(intent)?.let { locationAvailability ->

                if (!locationAvailability.isLocationAvailable) {
                    Log.d(TAG, "Location services are no longer available!")
                }
            } ?: Log.e(TAG, "Error loading location")

            LocationResult.extractResult(intent)?.let { locationResult ->
                val locations = locationResult.locations.map { location ->
                    LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        bearing = location.bearing,
                        isForeground = isAppInForeground(context)
                    )
                }
                if (locations.isNotEmpty()) {
                    Log.d(TAG, "Sending Location")
                    repository.sendLocations(locations)
                }
            } ?: Log.e(TAG, "Error loading result")
        }
    }

    // Note: This function's implementation is only for debugging purposes. If you are going to do
    // this in a production app, you should instead track the state of all your activities in a
    // process via android.app.Application.ActivityLifecycleCallbacks's
    // unregisterActivityLifecycleCallbacks(). For more information, check out the link:
    // https://developer.android.com/reference/android/app/Application.html#unregisterActivityLifecycleCallbacks(android.app.Application.ActivityLifecycleCallbacks
    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        appProcesses.forEach { appProcess ->
            if (appProcess.importance ==
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                appProcess.processName == context.packageName) {
                return true
            }
        }
        return false
    }

    companion object {
        const val ACTION_PROCESS_UPDATES =
            "com.mevron.rides.driver.location.location.LocationUpdatesBroadcastReceiver.action." +
                    "PROCESS_UPDATES"
    }
}