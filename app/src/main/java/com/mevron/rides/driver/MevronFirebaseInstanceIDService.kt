package com.mevron.rides.driver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mevron.rides.driver.home.data.model.home.DeviceID
import com.mevron.rides.driver.home.data.network.HomeScreenApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MevronFirebaseInstanceIDService: FirebaseMessagingService() {

    @Inject
    lateinit var fcmTokenUseCase: HomeScreenApi

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        fetchAndUpdateToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("TOKEN FOR FIREBASE", "TOKEN FOR FIREBASE RECEIVED")

        val audioAttributesDefault = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val title = message.notification?.title
        val text = message.notification?.body
        val intent =
            Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            application,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val channelId = "Default"
        val builder =
            NotificationCompat.Builder(application, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        val manager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#FF9B04")
            notificationChannel.enableVibration(true)
            notificationChannel.setSound(
                Uri.parse("android.resource://com.mevron.rides.driver/raw/ringtone2"),
                audioAttributesDefault)
            notificationChannel.vibrationPattern =
                longArrayOf(0, 100, 1000, 300, 200, 100, 500, 200, 100, 1000, 200, 500, 600, 590, 700, 1000, 800, 100)
            manager.createNotificationChannel(notificationChannel)
            //ringtone2
        }
        manager.notify(0, builder.build())

    }

    private fun fetchAndUpdateToken(token: String) {
        try {
            fcmTokenUseCase.updateToken(DeviceID(device_id = token)).execute()
        }catch (e: Throwable){
            e.printStackTrace()
        }
    }
}