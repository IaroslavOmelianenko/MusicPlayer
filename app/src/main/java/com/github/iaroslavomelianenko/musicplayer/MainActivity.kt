package com.github.iaroslavomelianenko.musicplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.iaroslavomelianenko.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        //Checking android version, creating channel for notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel",
                "Music Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }


        //Intent for actions
        val intent = Intent(this, MusicService::class.java)


        //Music player buttons
        _binding.ibPlay.setOnClickListener {
            intent.action = "PLAY"
            startService(intent)
        }

        _binding.ibStop.setOnClickListener {
            stopService(intent)
        }

        _binding.ibPause.setOnClickListener {
            intent.action = "PAUSE"
            startService(intent)
        }

        _binding.ibNext.setOnClickListener {
            intent.action = "NEXT"
            startService(intent)
        }

        _binding.ibPrevious.setOnClickListener {
            intent.action = "PREVIOUS"
            startService(intent)
        }
    }
}