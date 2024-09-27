package com.example.sample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrainingVideosActivity : AppCompatActivity() {

    // List of video titles and YouTube URLs
    private val videoList = listOf(
        VideoItem("Self-Defense Techniques for Women", "https://www.youtube.com/watch?v=KVpxP3ZZtAc"),
        VideoItem("SafeHer Campaign", "https://www.youtube.com/watch?v=w8RpxhtELNw"),
        VideoItem("How to Protect Yourself in Dangerous Situations", "https://www.youtube.com/watch?v=72-8jhREtzI"),
        VideoItem("Pepper Spray Defense Training", "https://www.youtube.com/watch?v=IxaXz_7OalM"),
        VideoItem("Self-Defense in Close Combat", "https://www.youtube.com/watch?v=Ww1DeUSC94o"),
        VideoItem("Advanced Self-Defense Moves", "https://www.youtube.com/watch?v=nep4mLyyQV8"),
        VideoItem("Street Survival Techniques", "https://www.youtube.com/watch?v=w8RpxhtELNw"),
        VideoItem("Personal Safety for Women", "https://www.youtube.com/watch?v=VePHquC2PrQ"),
        VideoItem("Street Survival Techniques", "https://www.youtube.com/watch?v=w8RpxhtELNw"),
        VideoItem("Personal Safety for Women", "https://www.youtube.com/watch?v=VePHquC2PrQ")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_videos)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Set up GridLayoutManager with 2 columns (4 videos in each column)
        val gridLayoutManager = GridLayoutManager(this, 2) // 2 columns
        recyclerView.layoutManager = gridLayoutManager

        // Set up the adapter for RecyclerView
        val adapter = VideoAdapter(videoList) { videoItem ->
            // Open YouTube video when clicked
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoItem.videoUrl))
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
