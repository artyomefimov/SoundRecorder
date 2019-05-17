package com.artyomefimov.soundrecorder.fragments.recordfragment

import android.os.Environment
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.services.recordservice.RecordService

object RecordController {
    private var isFirst = true
    lateinit var state: RecordButtonState

    enum class RecordButtonState(val resourceId: Int) {
        STARTED(R.drawable.ic_action_pause),
        STOPPED(R.drawable.ic_action_play)
    }

    private var isRecording = false
    var outputFilePath: String? = null
        set(folderPath) {
            field = "$folderPath"
        }

    fun init() {
        if (isFirst) {
            outputFilePath = Environment.getExternalStorageDirectory().absolutePath
            state =
                RecordController.RecordButtonState.STOPPED
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

    private fun startRecording() {
        isRecording = true
        state =
            RecordController.RecordButtonState.STARTED
    }

    private fun stopRecording() {
        isRecording = false
        state =
            RecordController.RecordButtonState.STOPPED
    }
}