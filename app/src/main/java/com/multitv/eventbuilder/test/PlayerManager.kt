package com.multitv.ott.player

import android.content.Context
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerManager(private val context: Context) {
    private var player: ExoPlayer? = null
    private var isMuted: Boolean = false
    private var bannerImageCard: CardView? = null

    @OptIn(UnstableApi::class)
    fun initializePlayer(playerView: PlayerView) {
        player = ExoPlayer.Builder(context).build()
        playerView.player = player

        player?.addListener(object :Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                      //  showBanner(bannerImageCard,playerView)
                    }
                    Player.STATE_BUFFERING -> {
                       // showBanner(bannerImageCard, playerView)
                    }
                    Player.STATE_READY -> {
                      //  hideBanner(bannerImageCard,playerView)
                    }
                    Player.STATE_ENDED -> {
                      //  hideBanner(bannerImageCard, playerView)
                    }

                }

            }
        })
    }

    private fun hideBanner(bannerImageCard: CardView, playerView: PlayerView) {
        bannerImageCard.isVisible = false
        playerView.isVisible = true
    }

    private fun showBanner(bannerView: CardView, playerView: PlayerView) {
        bannerView.isVisible = true
        playerView.isVisible = false
    }

    fun loadMedia(mediaUri: String) {
        val mediaItem = MediaItem.fromUri(mediaUri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
        player?.pause()

    }

    fun play() {
        player?.play() // Start playback
    }

    fun pause() {
        player?.pause() // Pause playback
    }

    private fun togglePlayPause() {
        // Toggle play/pause depending on the current state
        if (player?.isPlaying == true) {
            pause()
        } else {
            play()
        }
    }

    private fun mute() {
        // Mute the player (volume = 0)
        isMuted = true
        player?.volume = 0f
    }

    private fun unmute() {
        // Unmute the player (volume = 1)
        isMuted = false
        player?.volume = 1f
    }

    fun toggleMute() {
        // Toggle mute/unmute
        if (isMuted) {
            unmute()
        } else {
            mute()
        }
    }

    fun isPlaying(): Boolean {
        // Return whether the player is playing
        return player?.isPlaying == true
    }

    fun seekTo(position: Long) {
        // Seek to a specific position
        player?.seekTo(position)
    }

    fun getCurrentPosition(): Long {
        // Return the current position of the player
        return player?.currentPosition ?: 0L
    }

    fun getDuration(): Long {
        // Return the duration of the current media
        return player?.duration ?: 0L
    }

    fun release() {
        // Release the player when it's no longer needed
        player?.release()
        player = null
    }

    fun addListener(listener: Player.Listener) {
        // Add a listener to the player
        player?.addListener(listener)
    }

    fun updateSeekBarProgress(seekBar: SeekBar) {
        // This method will update the seek bar as the video plays
        val currentPosition = getCurrentPosition()
        val duration = getDuration()

        if (duration > 0) {
            val progress = (currentPosition * 100 / duration).toInt()
            seekBar.progress = progress
        }
    }
}
