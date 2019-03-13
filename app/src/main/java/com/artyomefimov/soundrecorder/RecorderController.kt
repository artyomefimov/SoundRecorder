package com.artyomefimov.soundrecorder

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import java.io.IOException

object RecorderController {
    private var isFirst = true
    lateinit var currentState: RecordButtonState

    enum class RecordButtonState(val resourceId: Int) {
        STARTED(R.drawable.ic_action_pause),
        STOPPED(R.drawable.ic_action_play)
    }

    private const val TAG = "RecordActivity"

    private var isRecording: Boolean = false
    private var mediaRecorder: MediaRecorder? = null
    private var output: String? = null

    fun init() {
        if (isFirst) {
            output =
                Environment.getExternalStorageDirectory().absolutePath + "/record.mp3"
            resetMediaRecorder()
            currentState = RecorderController.RecordButtonState.STOPPED
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
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
    }

    private fun startRecording() {
        try {
            resetMediaRecorder()
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true

            Log.i(TAG, "Recording was started!")
            currentState = RecorderController.RecordButtonState.STARTED
        } catch (e: IOException) {
            Log.i(TAG, "Could not start recording!")
            Log.e(TAG, e.localizedMessage)
            currentState = RecorderController.RecordButtonState.STOPPED
        }
    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        isRecording = false

        Log.i(TAG, "Recording was stopped!")

        currentState = RecorderController.RecordButtonState.STOPPED
    }
}