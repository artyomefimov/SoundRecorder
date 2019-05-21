package com.artyomefimov.soundrecorder.services.playservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.activity.RecordActivity
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.ACTION_NOTIFICATION_PAUSE
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.ACTION_NOTIFICATION_PLAY
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.ACTION_NOTIFICATION_STOP
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.CHANNEL_ID
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.CHANNEL_NAME
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.REQUEST_CODE_ACTIVITY
import com.artyomefimov.soundrecorder.services.playservice.PlayService.Companion.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun PlayService.createNotification(fileName: String): Notification {
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
        .setContentTitle(fileName)
        .setSmallIcon(R.mipmap.ic_launcher_foreground)
        .setAutoCancel(false)
        .addAction(
            android.R.drawable.ic_media_pause,
            resources.getString(R.string.pause),
            createNotificationActionIntent(ACTION_NOTIFICATION_PAUSE)
        )
        .addAction(
            android.R.drawable.ic_media_play,
            resources.getString(R.string.play),
            createNotificationActionIntent(ACTION_NOTIFICATION_PLAY)
        )
        .addAction(
            R.drawable.ic_media_stop,
            resources.getString(R.string.stop),
            createNotificationActionIntent(ACTION_NOTIFICATION_STOP)
        )
        .build()
}

internal fun PlayService.createNotificationActionIntent(action: String): PendingIntent {
    val intent = Intent(this, PlayService::class.java)
    intent.action = action
    return PendingIntent.getService(this, 0, intent, 0)
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

internal fun PlayService.startPlaying(filePath: String) = launch {
    delay(200)
    mediaPlayer?.let {
        if (it.isPlaying)
            stopPlaying()
    }

    mediaPlayer = MediaPlayer().apply {
        setDataSource(filePath)
        prepare()
        start()
        setOnCompletionListener {
            stopService()
        }
    }
    Log.i(TAG, "Playing was started!")
}

private fun PlayService.stopPlaying() {
    mediaPlayer?.apply {
        stop()
        reset()
        release()
    }
    mediaPlayer = null

    Log.i(TAG, "Playing was stopped!")
}

internal fun PlayService.pausePlaying() {
    mediaPlayer?.let {
        if (it.isPlaying)
            it.pause()
        Log.i(TAG, "Playing was paused!")
    }
}

internal fun PlayService.continuePlaying() {
    mediaPlayer?.let {
        if (!it.isPlaying)
            it.start()
        Log.i(TAG, "Playing was continued!")
    }
}

internal fun PlayService.stopService() {
    stopForeground(true)
    stopPlaying()
    stopSelf()
}