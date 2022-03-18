package com.mevron.rides.driver

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mevron.rides.driver.auth.AuthActivity
import com.mevron.rides.driver.remote.socket.SocketHandler
import com.mevron.rides.driver.ride.RideActivity
import com.mevron.rides.driver.util.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val editor = sPref.edit()
        editor.putString(Constants.TOKEN, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiZW1haWwiOiIiLCJuYW1lIjoiIiwidXVpZCI6Ijg3Yjg2YTA1LTQ1Y2ItNDBkZS1hMWJmLTkyZmQ4MzYyNTg4OCIsInBob25lTnVtYmVyIjoiMjM0NzAzMzUwNTAxMyIsInR5cGUiOiJkcml2ZXIiLCJpYXQiOjE2NDYzNTY4NDh9.icyUCSLOxulAxTsNl_AqJzY4E4_9YXxl9zf_1LaTXcQ")
        editor.putString(Constants.UUID, "87b86a05-45cb-40de-a1bf-92fd83625888")
        editor.apply()
      //  SocketHandler.setSocket("", "", "")
       // SocketHandler.establishConnection()
        val token = sPref.getString(Constants.TOKEN, null)
        val uuid = sPref.getString(Constants.UUID, null)
        if (token.isNullOrEmpty() || uuid.isNullOrEmpty()){
            startActivity(Intent(this, IntroActivity::class.java))
        }else{
            startActivity(Intent(this, RideActivity::class.java))
        }

        finish()
    }
}