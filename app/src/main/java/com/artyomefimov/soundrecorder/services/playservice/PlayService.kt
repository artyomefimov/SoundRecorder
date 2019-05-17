package com.artyomefimov.soundrecorder.services.playservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import java.io.File

class PlayService : Service() {
    internal var mediaPlayer: MediaPlayer? = null

    companion object {
        internal const val TAG = "RecordService"
        internal const val REQUEST_CODE_ACTIVITY = 456
        private const val REQUEST_CODE_FOREGROUND = 666
        internal const val CHANNEL_ID = "com.artyomefimov.soundrecorder.services.playservice.PlayService"
        internal const val CHANNEL_NAME = "SoundRecorder play"

        const val ACTION_START_PLAY = "start_play"
        const val ACTION_STOP_PLAY = "stop_play"
        const val FILE_NAME = "file_name"
        const val FILE_PATH = "file_path"
        const val ACTION_NOTIFICATION_PLAY = "play"
        const val ACTION_NOTIFICATION_PAUSE = "pause"
        const val ACTION_NOTIFICATION_STOP = "stop"
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                ACTION_START_PLAY -> start(intent)
                ACTION_STOP_PLAY -> stop()
                ACTION_NOTIFICATION_PAUSE -> pausePlaying()
                ACTION_NOTIFICATION_PLAY -> continuePlaying()
                ACTION_NOTIFICATION_STOP -> stopService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(intent: Intent) {
        Log.d(TAG, "Starting play service...")

        val extras = intent.extras ?: return
        val fileName = extras.get(FILE_NAME) as String
        val filePath = extras.get(FILE_PATH) as String

        val notification = createNotification(fileName)

        startPlaying(filePath + File.separator + fileName)
        startForeground(REQUEST_CODE_FOREGROUND, notification)
    }

    private fun stop() {
        Log.d(TAG, "Stopping play service...")
        stopService()
    }
}