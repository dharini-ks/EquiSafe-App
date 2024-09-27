package com.example.sample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var isVisible = false // Tracks visibility state
    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val currentItem = viewPager.currentItem
            val nextItem = (currentItem + 1) % (viewPager.adapter?.itemCount ?: 1)
            viewPager.setCurrentItem(nextItem, true)
            handler.postDelayed(this, 4000) // Change image every 3 seconds
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.floatingActionButton)
        val imageView9: ImageView = findViewById(R.id.imageView9)
        val imageView12: ImageView = findViewById(R.id.imageView12)
        val imageView13: ImageView = findViewById(R.id.imageView13)
        val imageView14: ImageView = findViewById(R.id.imageView14)
        val sosGif: ImageView = findViewById(R.id.sosGif)
        val imageView5: ImageView = findViewById(R.id.imageView5)
        val imageView4: ImageView = findViewById(R.id.imageView4)
        val imageView6: ImageView = findViewById(R.id.imageView6)
        val imageView2: ImageView = findViewById(R.id.imageView2)
        viewPager = findViewById(R.id.imageCarousel)

        // Set up ViewPager2 for carousel
        val images = listOf(
            R.drawable.img_30,
            R.drawable.img_27,
            R.drawable.img_31
        )
        val adapter = CarouselAdapter(images)
        viewPager.adapter = adapter

        // Start auto-scrolling
        handler.post(autoScrollRunnable)

        fab.setOnClickListener {
            isVisible = !isVisible // Toggle visibility state
            val visibility = if (isVisible) View.VISIBLE else View.GONE

            imageView9.visibility = visibility
            imageView12.visibility = visibility
            imageView13.visibility = visibility
            imageView14.visibility = visibility
        }

        // Load the GIF into ImageView using Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.sosbutton) // Replace with your GIF file name
            .into(sosGif)

        // Set onClickListener to navigate to MapsActivity
        sosGif.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener to navigate to SafePlacesActivity
        imageView5.setOnClickListener {
            val intent = Intent(this, SafePlacesActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener to navigate to ContactBookActivity
        imageView4.setOnClickListener {
            val intent = Intent(this, ContactBookActivity::class.java)
            startActivity(intent)
        }

        // Set onClickListener to navigate to UserProfileActivity
        imageView6.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        imageView9.setOnClickListener {
            val intent = Intent(this, TipsActivity::class.java)
            startActivity(intent)
        }

        imageView12.setOnClickListener {
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
        }

        imageView13.setOnClickListener {
            val intent = Intent(this, InstructionActivity::class.java)
            startActivity(intent)
        }

        imageView14.setOnClickListener {
            val intent = Intent(this, TrainingVideosActivity::class.java)
            startActivity(intent)
        }

        imageView2.setOnClickListener {
            val amazonUrl = "https://www.amazon.com/s?k=self+defense+tools+pepper+spray"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(amazonUrl))
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(autoScrollRunnable) // Stop auto-scrolling when the activity is destroyed
    }
}
