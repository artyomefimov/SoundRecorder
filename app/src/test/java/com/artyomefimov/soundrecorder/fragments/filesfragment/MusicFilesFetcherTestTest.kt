package com.artyomefimov.soundrecorder.fragments.filesfragment

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File

class MusicFilesFetcherTestTest {
    private val emptyFolderFiles = ArrayList<String>()

    private val onlyFormatFolderFiles = ArrayList<String>().apply {
        add("")
        add("a1.sqa")
        add("q1sdf.as")
        add("assddf.mp3")
        add("1212.txt")
        add("assdfdggjj,k..mp3")
        add("somefile.file")
    }

    private val multipleFormatFolderFiles = ArrayList<String>().apply {
        add("")
        add("a1.sqa")
        add("dfdfdgf.wav")
        add("q1sdf.as")
        add("assddf.mp3")
        add("1212.txt")
        add("4546576u.wma")
        add("assdfdggjj,k..mp3")
        add("somefile.file")
        add("111111")
        add("22314.ogg")
    }

    private val emptyFolder: File = mock()
    private val onlyFormatFolder: File = mock()
    private val multipleFormatFolder: File = mock()

    @Before
    fun setUp() {
        whenever(emptyFolder.list()).thenReturn(emptyFolderFiles.toTypedArray())
        whenever(onlyFormatFolder.list()).thenReturn(onlyFormatFolderFiles.toTypedArray())
        whenever(multipleFormatFolder.list()).thenReturn(multipleFormatFolderFiles.toTypedArray())

        whenever(emptyFolder.absolutePath).thenReturn("emptyFolder")
        whenever(onlyFormatFolder.absolutePath).thenReturn("onlyFormatFolder")
        whenever(multipleFormatFolder.absolutePath).thenReturn("multipleFormatFolder")
    }

    @Test
    fun getNoFilesFromEmptyFolder() {
        val fileInfo = fetchMusicFilesFromFolder(emptyFolder)

        assertEquals(0, fileInfo.size)
    }

    @Test
    fun getTwoMusicFilesFromOnlyFormatFolder() {
        val fileInfo = fetchMusicFilesFromFolder(onlyFormatFolder)

        assertEquals(2, fileInfo.size)
    }

    @Test
    fun getFiveFilesFromMultipleFormatFolder() {
        val fileInfo = fetchMusicFilesFromFolder(multipleFormatFolder)

        assertEquals(5, fileInfo.size)
    }
}