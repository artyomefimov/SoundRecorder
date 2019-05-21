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
    var listener: RecordFragment.ControllerStateListener? = null

    fun init(listener: RecordFragment.ControllerStateListener?) {
        if (isFirst) {
            outputFilePath = Environment.getExternalStorageDirectory().absolutePath
            state =
                RecordController.RecordButtonState.STOPPED
            isFirst = false

            this.listener = listener
        }
    }

    fun getNewRecordAction(): String {
        return if (!isRecording) {
            RecordService.ACTION_START_RECORD
        } else {
            RecordService.ACTION_STOP_RECORD
        }
    }

    fun startRecording() {
        isRecording = true
        state =
            RecordController.RecordButtonState.STARTED
        listener?.onStateChanged()
    }

    fun stopRecording() {
        isRecording = false
        state =
            RecordController.RecordButtonState.STOPPED
        listener?.onStateChanged()
    }
}