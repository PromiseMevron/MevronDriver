package com.mevron.rides.driver.remote.socket

import android.content.Context
import com.mevron.rides.driver.App
import com.mevron.rides.driver.util.Constants
import io.socket.client.IO
import io.socket.client.Socket

import java.net.URISyntaxException

object SocketHandler {

     var mSocket: Socket? = null

    @Synchronized
    fun setSocket(uiid: String, lat: String, lng: String) {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use the your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
            val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
            val uuid = sPref.getString(Constants.UUID, null)
            mSocket = IO.socket("http://staging.mevron.com:8083/")
        } catch (e: URISyntaxException) {
            print("the respone is error $e")

        }
    }

    @Synchronized
    fun getSocket(): Socket? {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket?.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket?.disconnect()
    }
}