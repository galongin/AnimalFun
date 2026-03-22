package com.animalfun.util

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

/**
 * Utility wrapper around [MediaPlayer] for playing raw audio resources.
 * Handles resource lookup by name, lifecycle management, and error recovery.
 */
class AudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private const val TAG = "AudioPlayer"
    }

    /**
     * Plays a raw resource identified by its resource name (e.g. "sound_cow").
     * If another sound is currently playing it is stopped first.
     *
     * @param resName the name of the raw resource (without "R.raw." prefix)
     * @param onCompletion optional callback invoked when playback finishes
     */
    fun play(resName: String, onCompletion: (() -> Unit)? = null) {
        stop()
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        if (resId == 0) {
            Log.w(TAG, "Sound resource not found: '$resName'. Using placeholder or skipping.")
            onCompletion?.invoke()
            return
        }

        try {
            mediaPlayer = MediaPlayer.create(context, resId)?.apply {
                setOnCompletionListener {
                    release()
                    mediaPlayer = null
                    onCompletion?.invoke()
                }
                setOnErrorListener { mp, _, _ ->
                    mp.release()
                    mediaPlayer = null
                    onCompletion?.invoke()
                    true
                }
                start()
            }
        } catch (_: Exception) {
            // Guard against any MediaPlayer init failures
            mediaPlayer?.release()
            mediaPlayer = null
            onCompletion?.invoke()
        }
    }

    /**
     * Plays a raw resource identified by its integer resource ID.
     */
    fun play(resId: Int) {
        if (resId == 0) return
        stop()
        try {
            mediaPlayer = MediaPlayer.create(context, resId)?.apply {
                setOnCompletionListener { release() }
                setOnErrorListener { mp, _, _ ->
                    mp.release()
                    mediaPlayer = null
                    true
                }
                start()
            }
        } catch (_: Exception) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    /** Stops any currently playing audio and releases the player. */
    fun stop() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        } catch (_: Exception) {
            // Already released or in an error state
        }
        mediaPlayer = null
    }

    /** Release resources – call from ViewModel.onCleared(). */
    fun release() {
        stop()
    }
}
