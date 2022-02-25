package com.mevron.rides.driver.service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.AuthActivity
import com.mevron.rides.driver.remote.socket.SocketHandler
import io.socket.client.Socket

class LocationService: Service() {


    private val binder = LocationLocalBinder()
    var context: Context? = null
    lateinit var mSocket: Socket


    inner class LocationLocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods

        fun getService(): LocationService {
            return this@LocationService
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        context = this
        SocketHandler.setSocket(uiid = "", lng = "", lat = "")
        SocketHandler.establishConnection()
        mSocket = SocketHandler.getSocket()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                "Mevron Rider",
                "Mevron", NotificationManager.IMPORTANCE_HIGH
            )
            chan.lightColor = Color.YELLOW
            chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
        }

        val pendingIntent: PendingIntent =
            Intent(this, AuthActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE,)
            }

        val notification: Notification = Notification.Builder(this, "Mevron Rider")
            .setContentTitle("Mevron Driver")
            .setContentText("Mevron Driver")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(pendingIntent)
            .setTicker("Mevron Driver")
            .setChannelId("Mevron Rider")
            .build()

// Notification ID cannot be 0.
        startForeground(1, notification)
    }




    override fun onBind(p0: Intent?): IBinder {
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                    mLocation = location
                    mLocation?.let {
                       // send location to backend
                       // mSocket.emit("event", 1)

                    }
                }
            })


        return START_STICKY
    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }
}