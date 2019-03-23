package com.artyomefimov.soundrecorder.activity

import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.controller.RecorderController
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

fun RecordActivity.updateButtonsState() {
    buttonState = RecorderController.state
    record_button.setImageResource(buttonState.resourceId)
    choose_folder_button.isEnabled = buttonState == RecorderController.RecordButtonState.STOPPED
}

fun RecordActivity.showToastIfFinished() {
    if (buttonState == RecorderController.RecordButtonState.STOPPED)
        toast(R.string.recording_finished)
}

fun RecordActivity.isPermissionNotGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
}

fun RecordActivity.getFolderPathViewText(path: String?): String = "${resources.getString(R.string.writing_to)} $path"