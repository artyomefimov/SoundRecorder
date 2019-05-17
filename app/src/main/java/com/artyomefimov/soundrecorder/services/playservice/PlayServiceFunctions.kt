package com.artyomefimov.soundrecorder.services.playservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.activity.RecordActivity
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.CHANNEL_ID
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.CHANNEL_NAME
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.REQUEST_CODE_ACTIVITY
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.TAG

internal fun PlayService.createNotification(): Notification {
    val intent = Intent(this, RecordActivity::class.java)
    intent.action = RecordActivity.PLAYING

    val pendingIntent =
        PendingIntent.getActivity(
            this,
            REQUEST_CODE_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

    createNotificationChannel()

    return NotificationCompat.Builder(
        this,
        CHANNEL_ID
    )
        .setContentIntent(pendingIntent)
        .setContentTitle("Playing...")
        .setSmallIcon(R.drawable.ic_notification_icon)
        .setAutoCancel(false)
        .build()
}

private fun PlayService.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME, importance
        )
        val notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}

internal fun PlayService.startPlaying(fileName: String) {
    try {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(fileName)
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            prepare()
            start()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    Log.i(TAG, "Playing was started!")
}

internal fun PlayService.stopPlaying() {
    mediaPlayer?.apply {
        stop()
        reset()
        release()
    }
    mediaPlayer = null

    Log.i(TAG, "Playing was stopped!")
}