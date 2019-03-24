package com.artyomefimov.soundrecorder.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.controller.RecorderController
import com.codekidlabs.storagechooser.StorageChooser
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


class RecordActivity : AppCompatActivity() {

    companion object {
        internal const val PERMISSIONS_REQUEST_CODE = 123
    }

    internal var isPermissionGranted: Boolean = false
    internal var buttonState: RecorderController.RecordButtonState =
        RecorderController.RecordButtonState.STOPPED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionsIfNeeded()

        var storageChooser =
            buildStorageChooserWithNewPath(Environment.getExternalStorageDirectory().absolutePath)

        storageChooser.setOnSelectListener {
            RecorderController.outputFilePath = it
            folder_path_view.text = getFolderPathViewText(it)
            storageChooser = buildStorageChooserWithNewPath(it)
        }

        folder_path_view.setOnClickListener {
            if (isNowNotRecording())
                storageChooser.show()
        }

        RecorderController.init()

        updateButtonsState()

        record_button.setOnClickListener {
            if (isPermissionGranted) {
                RecorderController.handleClick()
                updateButtonsState()
                showToastIfFinished()
            } else {
                toast(R.string.permissions_not_granted)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true
        }
    }
}
