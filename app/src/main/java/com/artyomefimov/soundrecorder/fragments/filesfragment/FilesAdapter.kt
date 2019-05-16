package com.artyomefimov.soundrecorder.fragments.filesfragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artyomefimov.soundrecorder.R
import com.artyomefimov.soundrecorder.fragments.recordfragment.RecorderController
import com.artyomefimov.soundrecorder.model.FileInfo
import kotlinx.android.synthetic.main.list_item_file.view.*

class FilesAdapter(private val files: List<FileInfo>, private val listener: Listener):
    RecyclerView.Adapter<FilesAdapter.FilesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_file, parent, false)
        return FilesViewHolder(itemView)
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) = holder.bind(files[position],listener)

    class FilesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fileInfo: FileInfo, listener: Listener) {
            with(itemView) {
                file_name.text = fileInfo.name
                file_folder.text = RecorderController.outputFilePath

                setOnClickListener {
                    listener.onClickItem(fileInfo)
                }
            }
        }
    }

    interface Listener {
        fun onClickItem(fileInfo: FileInfo)
    }
}