package com.artyomefimov.soundrecorder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class RecordActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE = 10
    }

    private var isPermissionGranted: Boolean = false
    private var buttonState: RecorderController.RecordButtonState = RecorderController.RecordButtonState.STOPPED

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

            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
        } else {
            isPermissionGranted = true
        }

        RecorderController.init()

        updateButtonState()

        button_record.setOnClickListener {
            if (isPermissionGranted) {
                RecorderController.handleClick()
                updateButtonState()
                showToastIfFinished()
            } else {
                toast("Permissions not granted!")
            }
        }
    }

    private fun updateButtonState() {
        buttonState = RecorderController.currentState
        button_record.setImageResource(buttonState.resourceId)
    }

    private fun showToastIfFinished() {
        if (buttonState == RecorderController.RecordButtonState.STOPPED)
            toast("Recording was finished!")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true
        }
    }

    private fun isPermissionNotGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
    }
}
