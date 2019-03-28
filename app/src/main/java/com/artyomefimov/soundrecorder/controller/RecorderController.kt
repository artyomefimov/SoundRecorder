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

    private fun startRecording() {
        try {
            resetMediaRecorder()
            mediaRecorder?.prepare()
            mediaRecorder?.start()

            isRecording = true
            state =
                RecorderController.RecordButtonState.STARTED

            Log.i(TAG, "Recording was started!")
        } catch (e: IOException) {
            Log.i(TAG, "Could not start recording!")
            Log.e(TAG, e.toString())
            state =
                RecorderController.RecordButtonState.STOPPED
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.stop()
        } catch (e: RuntimeException) {
            Log.e(TAG, e.toString())
        }
        mediaRecorder?.reset()
        mediaRecorder?.release()
        mediaRecorder = null

        isRecording = false
        state =
            RecorderController.RecordButtonState.STOPPED

        Log.i(TAG, "Recording was stopped!")
    }

    private fun resetMediaRecorder() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile("$outputFilePath/${getNameAccordingToDate()}")
        }
    }
}