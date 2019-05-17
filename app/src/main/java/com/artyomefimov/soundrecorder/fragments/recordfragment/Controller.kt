package com.artyomefimov.soundrecorder.fragments.recordfragment

import android.os.Environment
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.services.playservice.PlayService
import com.artyomefimov.soundrecorder.services.recordservice.RecordService

object Controller {
    private var isFirst = true
    lateinit var state: RecordButtonState

    enum class RecordButtonState(val resourceId: Int) {
        STARTED(R.drawable.ic_action_pause),
        STOPPED(R.drawable.ic_action_play)
    }

    private var isRecording = false
    private var isPlaying = false
    var outputFilePath: String? = null
        set(folderPath) {
            field = "$folderPath"
        }

    fun init() {
        if (isFirst) {
            outputFilePath = Environment.getExternalStorageDirectory().absolutePath
            state =
                Controller.RecordButtonState.STOPPED
            isFirst = false
        }
    }

    fun getNewRecordAction(): String {
        return if (!isRecording) {
            startRecording()
            RecordService.ACTION_START_RECORD
        } else {
            stopRecording()
            RecordService.ACTION_STOP_RECORD
        }
    }

    fun getNewPlayAction(): String {
        return if (!isPlaying)
            PlayService.ACTION_START_PLAY
        else
            PlayService.ACTION_STOP_PLAY
    }

    private fun startRecording() {
        isRecording = true
        state =
            Controller.RecordButtonState.STARTED
    }

    private fun stopRecording() {
        isRecording = false
        state =
            Controller.RecordButtonState.STOPPED
    }
}