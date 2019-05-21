package com.artyomefimov.soundrecorder.fragments.filesfragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecordController
import com.artyomefimov.soundrecorder.model.FileInfo
import com.artyomefimov.soundrecorder.services.playservice.PlayService
import kotlinx.android.synthetic.main.files_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FilesFragment : Fragment() {
    private var files: List<FileInfo> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.files_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        files_list.layoutManager = LinearLayoutManager(this.activity)

        files_list.adapter = FilesAdapter(files, object : FilesAdapter.Listener {
            override fun onClickItem(fileInfo: FileInfo) {
                playSelectedFile(fileInfo)
            }
        })

        fetchFiles()
    }

    private fun playSelectedFile(fileInfo: FileInfo) {
        val intent = Intent(this@FilesFragment.activity, PlayService::class.java)
        intent.action = PlayService.ACTION_START_PLAY

        intent.putExtra(PlayService.FILE_NAME, fileInfo.name)
        intent.putExtra(PlayService.FILE_PATH, fileInfo.folder)

        this@FilesFragment.activity?.startService(intent)
    }

    private fun fetchFiles() = CoroutineScope(Dispatchers.Main).launch {
        withContext(Dispatchers.IO) {
            files = fetchMusicFilesFromFolder(File(RecordController.outputFilePath))
        }
        val adapter = files_list.adapter as FilesAdapter
        adapter.files = files
        adapter.notifyDataSetChanged()
    }
}