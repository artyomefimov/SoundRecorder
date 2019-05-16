package com.artyomefimov.soundrecorder.fragments.recordfragment

import android.os.Environment
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.service.RecordService

object RecorderController {
    private var isFirst = true
    lateinit var state: RecordButtonState

    enum class RecordButtonState(val resourceId: Int) {
        STARTED(R.drawable.ic_action_pause),
        STOPPED(R.drawable.ic_action_play)
    }

    private var isRecording: Boolean = false
    var outputFilePath: String? = null
        set(folderPath) {
            field = "$folderPath"
        }

    fun init() {
        if (isFirst) {
            outputFilePath = Environment.getExternalStorageDirectory().absolutePath
            state =
                RecorderController.RecordButtonState.STOPPED
            isFirst = false
        }
    }

    fun getNewAction(): String {
        return if (!isRecording) {
            startRecording()
            RecordService.ACTION_START_RECORD
        } else {
            stopRecording()
            RecordService.ACTION_STOP_RECORD
        }
    }

    private fun startRecording() {
        isRecording = true
        state =
            RecorderController.RecordButtonState.STARTED
    }

    private fun stopRecording() {
        isRecording = false
        state =
            RecorderController.RecordButtonState.STOPPED
    }
}