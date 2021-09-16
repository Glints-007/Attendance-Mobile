package com.example.attendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.attendance.R
import com.google.android.material.imageview.ShapeableImageView

class SplashScreen : AppCompatActivity() {
    private lateinit var splash: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splash = findViewById(R.id.splash)
        splash.animate().alpha(4000F).setDuration(0)

        Handler().postDelayed({
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}