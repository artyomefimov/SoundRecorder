package com.artyomefimov.soundrecorder.services.recordservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.activity.MainActivity
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecordController
import com.artyomefimov.soundrecorder.services.getNameAccordingToDate
import com.artyomefimov.soundrecorder.services.recordservice.RecordService.Companion.ACTION_NOTIFICATION_STOP
import com.artyomefimov.soundrecorder.services.recordservice.RecordService.Companion.TAG
import java.io.IOException

internal fun RecordService.createNotification(): Notification {
    val intent = Intent(this, MainActivity::class.java)

    val pendingIntent =
        PendingIntent.getActivity(
            this,
            RecordService.REQUEST_CODE_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

    createNotificationChannel()

    return NotificationCompat.Builder(
        this,
        RecordService.CHANNEL_ID
    )
        .setContentIntent(pendingIntent)
        .setContentTitle(resources.getString(R.string.recording_in_progress))
        .setSmallIcon(R.mipmap.ic_launcher_foreground)
        .setAutoCancel(false)
        .addAction(
            R.drawable.ic_media_stop,
            resources.getString(R.string.stop),
            createNotificationActionIntent(ACTION_NOTIFICATION_STOP)
        )
        .build()
}

private fun RecordService.createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            RecordService.CHANNEL_ID,
            RecordService.CHANNEL_NAME, importance
        )
        val notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}

internal fun RecordService.createNotificationActionIntent(action: String): PendingIntent {
    val intent = Intent(this, RecordService::class.java)
    intent.action = action
    return PendingIntent.getService(this, 0, intent, 0)
}

internal fun RecordService.startRecording(filePath: String) {
    try {
        resetMediaRecorder(filePath)
        mediaRecorder?.apply {
            prepare()
            start()
        }
        RecordController.startRecording()
    } catch (e: IOException) {
        Log.e(TAG, "Could not start recording! $e")
        RecordController.stopRecording()
    }
    Log.i(TAG, "Recording was started!")
}

private fun RecordService.resetMediaRecorder(filePath: String) {
    mediaRecorder = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setOutputFile("$filePath/${getNameAccordingToDate()}")
    }
}

internal fun RecordService.stopRecording() {
    try {
        mediaRecorder?.stop()
    } catch (e: RuntimeException) {
        Log.e(TAG, e.toString())
    }
    mediaRecorder?.apply {
        reset()
        release()
    }
    mediaRecorder = null

    RecordController.stopRecording()

    Log.i(TAG, "Recording was stopped!")
}