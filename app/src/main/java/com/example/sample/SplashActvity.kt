package com.example.sample

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SplashActivity : AppCompatActivity() {

    private val splashTimeOut: Long = 3000 // Duration of splash screen in milliseconds (3 seconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageViewGif = findViewById<ImageView>(R.id.imageViewGif)



        // Handler to transition to the main activity after splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Swip1::class.java))
            finish()
        }, splashTimeOut)
    }
}
