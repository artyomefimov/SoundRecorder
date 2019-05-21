package com.artyomefimov.soundrecorder.fragments.recordfragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecordFragment.Companion.PERMISSIONS_REQUEST_CODE
import com.codekidlabs.storagechooser.Content
import com.codekidlabs.storagechooser.StorageChooser
import kotlinx.android.synthetic.main.record_fragment.*
import org.jetbrains.anko.toast

private lateinit var content: Content

internal fun RecordFragment.updateButtonsState() {
    buttonState = RecordController.state

    record_button.setImageResource(buttonState.resourceId)
    folder_path_view.text = getStringWithFolderPath(RecordController.outputFilePath)
}

internal fun RecordFragment.getStringWithFolderPath(path: String?): String =
    if (isNowNotRecording())
        resources.getString(R.string.text_view_not_recording, path)
    else
        resources.getString(R.string.text_view_recording, path)

internal fun RecordFragment.showFinishingToast() {
    this.activity?.toast(R.string.recording_finished)
}

internal fun RecordFragment.isNowNotRecording() = buttonState == RecordController.RecordButtonState.STOPPED

internal fun RecordFragment.buildContentForStorageChooser() {
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

internal fun RecordFragment.buildStorageChooserWithNewPath(path: String?): StorageChooser {
    return StorageChooser.Builder()
        .withActivity(this.activity)
        .withFragmentManager(this.activity?.fragmentManager)
        .allowCustomPath(true)
        .setType(StorageChooser.DIRECTORY_CHOOSER)
        .withMemoryBar(true)
        .withContent(content)
        .withPredefinedPath(path)
        .build()
}

internal fun RecordFragment.requestPermissionsIfNeeded() {
    if (isPermissionNotGranted(Manifest.permission.RECORD_AUDIO)
        || isPermissionNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    ) {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        requestPermissions(
            permissions,
            PERMISSIONS_REQUEST_CODE
        )
    } else {
        isPermissionGranted = true
    }
}

private fun RecordFragment.isPermissionNotGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this.activity as Activity, permission) != PackageManager.PERMISSION_GRANTED
}

