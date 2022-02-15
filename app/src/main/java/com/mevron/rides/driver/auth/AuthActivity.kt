package com.mevron.rides.driver.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentResolver
import com.mevron.rides.driver.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }
}