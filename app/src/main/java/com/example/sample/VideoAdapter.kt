package com.example.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VideoAdapter(
    private val videoList: List<VideoItem>,
    private val clickListener: (VideoItem) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoItem = videoList[position]
        holder.bind(videoItem, clickListener)
    }

    override fun getItemCount(): Int = videoList.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val videoTitle: TextView = itemView.findViewById(R.id.videoTitle)
        private val videoThumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)

        fun bind(videoItem: VideoItem, clickListener: (VideoItem) -> Unit) {
            videoTitle.text = videoItem.title

            // Load YouTube video thumbnail
            val thumbnailUrl = "https://img.youtube.com/vi/${getYouTubeVideoId(videoItem.videoUrl)}/0.jpg"
            Glide.with(itemView.context)
                .load(thumbnailUrl)
                .into(videoThumbnail)

            // Set click listener
            itemView.setOnClickListener { clickListener(videoItem) }
        }

        // Extract YouTube video ID from the URL
        private fun getYouTubeVideoId(videoUrl: String): String {
            return videoUrl.split("v=")[1] // This is a simple method, you can enhance it
        }
    }
}
