package com.artyomefimov.soundrecorder.fragments.filesfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecorderController
import com.artyomefimov.soundrecorder.model.FileInfo
import kotlinx.android.synthetic.main.files_fragment.*
import java.io.File
import java.util.*

class FilesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.files_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val files = fetchMusicFilesFromFolder(File(RecorderController.outputFilePath)) // todo to background

        files_list.layoutManager = LinearLayoutManager(this.activity)
        files_list.adapter = FilesAdapter(files, object : FilesAdapter.Listener {
            override fun onClickItem(fileInfo: FileInfo) {
                // todo call service that plays music file
            }
        })
    }
}