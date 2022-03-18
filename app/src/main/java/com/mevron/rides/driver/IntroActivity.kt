package com.mevron.rides.driver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mevron.rides.driver.auth.AuthActivity

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val button = findViewById<Button>(R.id.start_button)
        button.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}