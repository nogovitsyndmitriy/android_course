package com.gmail.task_10_mp3player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AudioPlayService() : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("CHANNEL", "My Background Service")
        }
        if (intent != null) {
            val song = intent.getIntExtra("song", 0)
            val songIndex = intent.getIntExtra("songIndex", 0)
            mediaPlayer = MediaPlayer.create(this, song, AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(), songIndex)
            createNotification()
            mediaPlayer.start()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }


    private fun createNotification() {
        val notification = NotificationCompat.Builder(applicationContext, "CHANNEL")
            .setContentTitle("media player works")
            .build()
        startForeground(666, notification)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}