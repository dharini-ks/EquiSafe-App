package com.example.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Swip3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introdu) // Your Police Support layout

        // Find the "Next" button
        val nextButton: Button = findViewById(R.id.btnnext)

        // Set an OnClickListener to navigate to the next activity
        nextButton.setOnClickListener {
            // Create an Intent to start the next activity (e.g., Swip2)
            val intent = Intent(this, Swip4::class.java)
            startActivity(intent)
        }
    }
}
