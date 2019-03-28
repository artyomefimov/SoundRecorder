package com.artyomefimov.soundrecorder.activity

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.controller.RecorderController
import com.codekidlabs.storagechooser.Content
import com.codekidlabs.storagechooser.StorageChooser
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

private lateinit var content: Content

fun RecordActivity.updateButtonsState() {
    buttonState = RecorderController.state

    record_button.setImageResource(buttonState.resourceId)
    folder_path_view.text = getStringWithFolderPath(RecorderController.outputFilePath)
}

fun RecordActivity.getStringWithFolderPath(path: String?): String =
    if (isNowNotRecording())
        resources.getString(R.string.text_view_not_recording, path)
    else
        resources.getString(R.string.text_view_recording, path)

fun RecordActivity.showToastIfFinished() {
    if (isNowNotRecording())
        toast(R.string.recording_finished)
}

fun RecordActivity.isNowNotRecording() = buttonState == RecorderController.RecordButtonState.STOPPED

fun RecordActivity.buildContentForStorageChooser() {
    content = Content().apply {
        createLabel = resources.getString(R.string.picker_create)
        internalStorageText = resources.getString(R.string.picker_internal_storage_text)
        cancelLabel = resources.getString(R.string.picker_cancel)
        selectLabel = resources.getString(R.string.picker_select)
        overviewHeading = resources.getString(R.string.picker_overview_heading)
        newFolderLabel = resources.getString(R.string.picker_create_folder)
        freeSpaceText = resources.getString(R.string.picker_free_disk_space)
        folderCreatedToastText = resources.getString(R.string.picker_folder_created_toast)
        folderErrorToastText = resources.getString(R.string.picker_error_folder_creating)
        textfieldHintText = resources.getString(R.string.picker_hint)
        textfieldErrorText = resources.getString(R.string.picker_empty_folder_name_toast)
    }
}

fun RecordActivity.buildStorageChooserWithNewPath(path: String?): StorageChooser {
    return StorageChooser.Builder()
        .withActivity(this)
        .withFragmentManager(fragmentManager)
        .allowCustomPath(true)
        .setType(StorageChooser.DIRECTORY_CHOOSER)
        .withMemoryBar(true)
        .withContent(content)
        .withPredefinedPath(path)
        .build()
}

fun RecordActivity.requestPermissionsIfNeeded() {
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
            RecordActivity.PERMISSIONS_REQUEST_CODE
        )
    } else {
        isPermissionGranted = true
    }
}

private fun RecordActivity.isPermissionNotGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
}

