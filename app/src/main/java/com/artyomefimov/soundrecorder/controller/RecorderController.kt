package com.artyomefimov.soundrecorder.controller

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.getNameAccordingToDate
import java.io.IOException

object RecorderController {
    private const val TAG = "RecordActivity"
    private var isFirst = true
    lateinit var state: RecordButtonState

    enum class RecordButtonState(val resourceId: Int) {
        STARTED(R.drawable.ic_action_pause),
        STOPPED(R.drawable.ic_action_play)
    }

    private var isRecording: Boolean = false
    private var mediaRecorder: MediaRecorder? = null
    var outputFilePath: String? = null
    set(folderPath) {
        field = "$folderPath"
        resetMediaRecorder()
    }

    fun init() {
        if (isFirst) {
            outputFilePath = Environment.getExternalStorageDirectory().absolutePath
            resetMediaRecorder()
            state =
                RecorderController.RecordButtonState.STOPPED
            isFirst = false
        }
    }

    fun handleClick() {
        if (!isRecording)
            startRecording()
        else
            stopRecording()
    }

    private fun resetMediaRecorder() {
        mediaRecorder = MediaRecorder()
        Log.i(TAG, mediaRecorder?.toString())
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile("$outputFilePath/${getNameAccordingToDate()}")
    }

    private fun startRecording() {
        try {
            resetMediaRecorder()
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true

            Log.i(TAG, "Recording was started!")
            state =
                RecorderController.RecordButtonState.STARTED
        } catch (e: IOException) {
            Log.i(TAG, "Could not start recording!")
            Log.e(TAG, e.localizedMessage)
            state =
                RecorderController.RecordButtonState.STOPPED
        }
    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        isRecording = false

        Log.i(TAG, "Recording was stopped!")

        state =
            RecorderController.RecordButtonState.STOPPED
    }
}