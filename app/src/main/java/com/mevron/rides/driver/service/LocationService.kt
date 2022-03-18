package com.mevron.rides.driver.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.AuthActivity
import com.mevron.rides.driver.remote.socket.SocketHandler
import com.mevron.rides.driver.util.Constants
import io.socket.client.Socket
import org.json.JSONObject

class LocationService: Service() {


    private val binder = LocationLocalBinder()
    var context: Context? = null
    private var mSocket: Socket? = null
    val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
    val uuid = sPref.getString(Constants.UUID, null)
    val lati = sPref.getString(Constants.LAT, null)
    val lng = sPref.getString(Constants.LNG, null)
    val tripID = sPref.getString(Constants.TRIP_ID, null)

    val isServiceStarted: MutableLiveData<Boolean> = MutableLiveData()


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
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
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

        isServiceStarted.value = true
        val uuid = sPref.getString(Constants.UUID, null)
        val lati = sPref.getString(Constants.LAT, null)
        val lng = sPref.getString(Constants.LNG, null)

        if (uuid != null && lati != null && lng != null) {
         //   mSocket = SocketHandler.getSocket()
        }

        mSocket = SocketHandler.getSocket()


        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                  //  Toast.makeText(context, "2323", Toast.LENGTH_SHORT).show()
                    mLocation = location
                    mLocation?.let {
                      //  Toast.makeText(context, "4444", Toast.LENGTH_SHORT).show()


                        val update = UpdateLocationModel(lat = it.latitude.toString(), long = it.longitude.toString(), uuid = uuid)
                            val json = Gson().toJson(update)
                        /**
                         *    val lat: String,
                        val long: String,
                        val uuid: String? = null,
                        val trip_id: String? = null
                         */

                        val jo = JSONObject()
                        jo.put("lat", it.latitude.toString())
                        jo.put("long", it.longitude.toString())
                        jo.put("uuid", uuid)
                        jo.put("trip_id", tripID)

                            mSocket?.emit("driver_location", jo)

                            val update2 = UpdateLocationModel(lat = it.latitude.toString(), long = it.longitude.toString(), trip_id = tripID)
                            val json2 = Gson().toJson(update2)
                            mSocket?.emit("trip_status", jo)


                    }
                }
            })


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted.value = false
    //   SocketHandler.closeConnection()

    }

    companion object {
        var mLocation: Location? = null

    }


}