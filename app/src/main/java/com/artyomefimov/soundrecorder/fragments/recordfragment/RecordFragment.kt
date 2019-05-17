package com.artyomefimov.soundrecorder.fragments.recordfragment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.services.recordservice.RecordService
import kotlinx.android.synthetic.main.record_fragment.*
import org.jetbrains.anko.toast

class RecordFragment: Fragment() {
    companion object {
        internal const val PERMISSIONS_REQUEST_CODE = 123
    }

    internal var isPermissionGranted: Boolean = false
    internal var buttonState: Controller.RecordButtonState =
        Controller.RecordButtonState.STOPPED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.record_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionsIfNeeded()

        buildContentForStorageChooser()
        var storageChooser =
            buildStorageChooserWithNewPath(Environment.getExternalStorageDirectory().absolutePath)

        Controller.init()

        updateButtonsState()

        storageChooser.setOnSelectListener {
            Controller.outputFilePath = it
            folder_path_view.text = getStringWithFolderPath(it)
            storageChooser = buildStorageChooserWithNewPath(it)
        }

        folder_path_view.setOnClickListener {
            if (isNowNotRecording())
                storageChooser.show()
        }

        record_button.setOnClickListener {
            if (isPermissionGranted) {
                val intent = Intent(this.activity, RecordService::class.java)

                intent.action = Controller.getNewRecordAction()

                updateButtonsState()

                if (isNowNotRecording()) {
                    this.activity?.startService(intent)
                    showToastIfFinished()
                } else {
                    intent.putExtra(RecordService.FILE_PATH, Controller.outputFilePath)
                    this.activity?.startService(intent)
                }
            } else {
                this.activity?.toast(R.string.permissions_not_granted)
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