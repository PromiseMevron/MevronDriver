package com.mevron.rides.driver.ride

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mevron.rides.driver.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride)
    }
}