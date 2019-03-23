package com.artyomefimov.soundrecorder.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.controller.RecorderController
import com.codekidlabs.storagechooser.StorageChooser
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


class RecordActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 123
    }

    private var isPermissionGranted: Boolean = false
    internal var buttonState: RecorderController.RecordButtonState =
        RecorderController.RecordButtonState.STOPPED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        if (isPermissionNotGranted(Manifest.permission.RECORD_AUDIO)
            && isPermissionNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {

            val permissions = arrayOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            ActivityCompat.requestPermissions(
                this, permissions,
                PERMISSIONS_REQUEST_CODE
            )
        } else {
            isPermissionGranted = true
        }

        val storageChooser = StorageChooser.Builder()
            .withActivity(this)
            .withFragmentManager(fragmentManager)
            .withPredefinedPath(Environment.getExternalStorageDirectory().absolutePath)
            .allowCustomPath(true)
            .setType(StorageChooser.DIRECTORY_CHOOSER)
            .build()

        storageChooser.setOnSelectListener {
            RecorderController.outputFilePath = it
            folder_path_view.text = getFolderPathViewText(it)
        }

        RecorderController.init()

        folder_path_view.text = getFolderPathViewText(RecorderController.outputFilePath)

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

        choose_folder_button.setOnClickListener {
            storageChooser.show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true
        }
    }
}
