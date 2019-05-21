package com.artyomefimov.soundrecorder.fragments.filesfragment

import com.artyomefimov.soundrecorder.model.FileInfo
import java.io.File

private val musicExtensions = listOf(".mp3", ".wav", ".wma", ".ogg")

fun getExtensionFromFileName(fileName: String): String {
    val pointIndex = fileName.lastIndexOf('.')
    return if (pointIndex == -1) "" else fileName.substring(pointIndex, fileName.length)
}

fun fetchMusicFilesFromFolder(folderPath: File): List<FileInfo> {
    val musicFiles = ArrayList<FileInfo>()
    folderPath.list().forEach { filename ->
        musicExtensions.forEach { extension ->
            if (extension == getExtensionFromFileName(filename))
                musicFiles.add(FileInfo(filename, folderPath.absolutePath))
        }
    }
    return musicFiles.reversed()
}