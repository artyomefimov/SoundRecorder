package com.artyomefimov.soundrecorder.service

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log

class RecordService : Service() {
    internal val TAG = "RecordService"
    internal var mediaRecorder: MediaRecorder? = null

    companion object Constants {
        internal const val REQUEST_CODE_ACTIVITY = 121
        private const val REQUEST_CODE_FOREGROUND = 242
        internal const val CHANNEL_ID = "com.artyomefimov.soundrecorder.service.RecordService"
        internal const val CHANNEL_NAME = "SoundRecorder app notifications"

        const val ACTION_START_RECORD = "start_record"
        const val ACTION_STOP_RECORD = "stop_record"
        const val FILE_PATH = "file_path"
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                ACTION_START_RECORD -> start(intent)
                ACTION_STOP_RECORD -> stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(intent: Intent?) {
        Log.d(TAG, "Starting service...")

        val extras = intent?.extras ?: return
        val filePath = extras.get(FILE_PATH) as String

        val notification = createNotification()

        startRecording(filePath)
        startForeground(REQUEST_CODE_FOREGROUND, notification)
    }

    private fun stop() {
        Log.d(TAG, "Stopping service...")

        stopForeground(true)
        stopRecording()
        stopSelf()
    }
}