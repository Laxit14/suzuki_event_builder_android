package com.multitv.eventbuilder.ui.home.adaptor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multitv.eventbuilder.R
import com.multitv.eventbuilder.model.homemodel.model.BannerData

class ImagePagerAdapter(
    private val imageList: List<BannerData>,
    private val listener: ImageClickListener
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
    private var currentPosition = -1
    private val players = mutableListOf<ExoPlayer>()
    interface ImageClickListener {
        fun onItemClick(imageItem: BannerData)
    }
    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val videoView: PlayerView = view.findViewById(R.id.videoView)
        private val textView: TextView = view.findViewById(R.id.imageText)
        private val muteButton: ImageButton = view.findViewById(R.id.muteButton)
        private var player: ExoPlayer? = null
        private var isMuted = false
        @OptIn(UnstableApi::class)
        fun bind(item: BannerData, isCurrent: Boolean) {
            itemView.setOnClickListener { listener.onItemClick(item) }
// Reset UI
            imageView.visibility = View.GONE
            videoView.visibility = View.GONE
            textView.visibility = View.GONE
            muteButton.visibility = View.GONE

            releasePlayer()

            val isVideo = isVideoFile(item.image)
            if (isVideoFile(item.image)) {
// Video setup
                videoView.visibility = View.VISIBLE
                muteButton.visibility = View.VISIBLE
                val context = videoView.context
                val mediaSourceFactory = DefaultMediaSourceFactory(context)
                player = ExoPlayer.Builder(context)
                    .setMediaSourceFactory(mediaSourceFactory)
                    .build().also {
                        videoView.player = it
                        players.add(it)
                    }
                val mediaItem = MediaItem.fromUri(item.image)
                player?.setMediaItem(mediaItem)
                player?.prepare()
                player?.playWhenReady = isCurrent
                isMuted = true
                setVolumeState()
                muteButton.setOnClickListener {
                    isMuted = !isMuted
                    setVolumeState()
                }
                player?.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            player?.seekTo(0)
                            player?.playWhenReady = true
                        }
                    }
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("ExoPlayer", "Playback error: ${error.message}")
                        fallbackToImage(item)
                    }
                })
            } else {
                fallbackToImage(item)
            }

            // Show title only if it's not a video
            if (!isVideo && !item.title.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = item.title
            } else {
                textView.visibility = View.GONE
            }
        }
        private fun fallbackToImage(item: BannerData) {
            imageView.visibility = View.VISIBLE
            muteButton.visibility = View.GONE
            Glide.with(imageView.context)
                .load(item.image)
                .into(imageView)
        }
        private fun setVolumeState() {
            player?.volume = if (isMuted) 0f else 1f
            muteButton.setImageResource(
                if (isMuted) R.drawable.unmute else R.drawable.mute
            )
        }
        fun releasePlayer() {
            player?.release()
            players.remove(player)
            player = null
            videoView.player = null
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viewpager_image, parent, false)
        return ImageViewHolder(view)
    }
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position], position == currentPosition)
    }
    override fun getItemCount(): Int = imageList.size
    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer()
    }
    fun onPageSelected(position: Int, recyclerView: RecyclerView) {
        if (currentPosition != position) {
            val previousPosition = currentPosition
            currentPosition = position
            recyclerView.post {
                if (previousPosition != -1) notifyItemChanged(previousPosition)
                notifyItemChanged(currentPosition)
            }
        }
    }
    fun releaseAllPlayers() {
        for (player in players) {
            player.release()
        }
        players.clear()
    }
    fun playPlayer() {
        notifyDataSetChanged()
    }
    private fun isVideoFile(url: String): Boolean {
        val videoExtensions = listOf("mp4", "m3u8", "mov", "avi")
        return videoExtensions.any { url.lowercase().endsWith(it) }
    }
}