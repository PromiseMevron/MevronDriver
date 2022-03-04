package com.mevron.rides.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mevron.rides.driver.auth.AuthActivity
import com.mevron.rides.driver.ride.RideActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, RideActivity::class.java))
        finish()
    }
}