package com.artyomefimov.soundrecorder.service

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
import com.artyomefimov.soundrecorder.activity.RecordActivity
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecorderController
import java.io.IOException
import java.util.*

internal fun RecordService.createNotification(): Notification {
    val intent = Intent(this, RecordActivity::class.java)

    val pendingIntent =
        PendingIntent.getActivity(
            this,
            RecordService.REQUEST_CODE_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

    createNotificationChannel()

    return NotificationCompat.Builder(this, RecordService.CHANNEL_ID)
        .setContentIntent(pendingIntent)
        .setContentTitle("Recording...")
        .setSmallIcon(R.drawable.ic_notification_icon)
        .setAutoCancel(false)
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

internal fun RecordService.startRecording(filePath: String) {
    try {
        resetMediaRecorder(filePath)
        mediaRecorder?.apply {
            prepare()
            start()
        }
    } catch (e: IOException) {
        Log.e(TAG, "Could not start recording!\n$e")
        RecorderController.state =
            RecorderController.RecordButtonState.STOPPED
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

private fun getNameAccordingToDate(): String {
    val calendar: Calendar = Calendar.getInstance()
    return "${calendar[Calendar.DAY_OF_MONTH]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}" +
            "_${calendar[Calendar.HOUR_OF_DAY]}.${calendar[Calendar.MINUTE]}.${calendar[Calendar.SECOND]}.mp3"
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

    RecorderController.state =
        RecorderController.RecordButtonState.STOPPED

    Log.i(TAG, "Recording was stopped!")
}