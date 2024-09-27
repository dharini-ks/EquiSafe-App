package com.example.sample

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support)

        // Find each section by its ID
        val section1: LinearLayout = findViewById(R.id.section1)
        val section2: LinearLayout = findViewById(R.id.section2)
        val section3: LinearLayout = findViewById(R.id.section3)
        val section4: LinearLayout = findViewById(R.id.section4)

        // Set onClick listeners for each section
        section1.setOnClickListener {
            val intent = Intent(this, PoliceSupportActivity::class.java)
            startActivity(intent)
        }

        section2.setOnClickListener {
            val intent = Intent(this, MentalSupportActivity::class.java)
            startActivity(intent)
        }

        section3.setOnClickListener {
            val intent = Intent(this, MedicalSupportActivity::class.java)
            startActivity(intent)
        }

        section4.setOnClickListener {
            val intent = Intent(this, LegalSupportActivity::class.java)
            startActivity(intent)
        }
    }
}
