package com.github.iaroslavomelianenko.musicplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    //Creating tracks
    private val track1 = Song(
        "Never Gonna Give You Up",
        "Rick Astley",
        R.raw.never_gonna_give_you_up,
        R.drawable.rick_astley
    )
    private val track2 = Song(
        "Big Enough",
        "Kirin J Callinan",
        R.raw.big_enough,
        R.drawable.kirin_j_callinan
    )


    //List of tracks
    private var tracks: ArrayList<Song> = arrayListOf(track1, track2)
    private var currentTrackIndex: Int = 0


    //Pause check
    private var isPaused: Boolean = false


    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, tracks[currentTrackIndex].songURI)
        mediaPlayer.isLooping = false
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> {
                if (isPaused) {
                    mediaPlayer.start()
                    isPaused = false
                } else {
                    mediaPlayer.release()
                    mediaPlayer = MediaPlayer.create(this, track1.songURI)
                    mediaPlayer.isLooping = false
                    mediaPlayer.start()
                }
                Toast.makeText(this, "PLAYING: ${tracks[currentTrackIndex].songTitle}", Toast.LENGTH_SHORT).show()
                showNotification()
            }

            "PAUSE" -> {
                mediaPlayer.pause()
                isPaused = true
                stopForeground(false)
                Toast.makeText(this, "Music paused", Toast.LENGTH_SHORT).show()
                showNotification()
            }

            "NEXT" -> {
                currentTrackIndex++
                if (currentTrackIndex >= tracks.size) {
                    currentTrackIndex = 0
                }
                mediaPlayer.release()
                mediaPlayer = MediaPlayer.create(this, tracks[currentTrackIndex].songURI)
                mediaPlayer.isLooping = false
                mediaPlayer.start()
                Toast.makeText(this, "NEXT: ${tracks[currentTrackIndex].songTitle}", Toast.LENGTH_SHORT).show()
                showNotification()
            }

            "PREVIOUS" -> {
                currentTrackIndex--
                if (currentTrackIndex < 0) {
                    currentTrackIndex = tracks.size - 1
                }
                mediaPlayer.release()
                mediaPlayer = MediaPlayer.create(this, tracks[currentTrackIndex].songURI)
                mediaPlayer.isLooping = false
                mediaPlayer.start()
                Toast.makeText(this, "PREVIOUS: ${tracks[currentTrackIndex].songTitle}", Toast.LENGTH_SHORT).show()
                showNotification()
            }
        }
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun showNotification() {
        val notification = NotificationCompat.Builder(this, "music_channel")
            .setContentTitle("Music Player")
            .setContentText("Playing Music")
            .setSmallIcon(R.drawable.ic_music)
            .addAction(
                R.drawable.ic_previous,
                "Previous",
                getPendingIntent("PREVIOUS")
            )
            .addAction(
                if (isPaused) R.drawable.ic_play else R.drawable.ic_pause,
                if (isPaused) "Play" else "Pause",
                getPendingIntent(if (isPaused) "PLAY" else "PAUSE")
            )
            .addAction(
                R.drawable.ic_next,
                "Next",
                getPendingIntent("NEXT")
            )
            .build()

        startForeground(1, notification)
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java)
        intent.action = action
        return PendingIntent.getService(this, 0, intent, 0)
    }
}
