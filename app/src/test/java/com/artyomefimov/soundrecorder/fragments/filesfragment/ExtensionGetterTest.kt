package com.artyomefimov.soundrecorder.fragments.filesfragment

import org.junit.Assert.*
import org.junit.Test

class ExtensionGetterTest{

    @Test
    fun getCorrectExtensionFromFilenameWithOnePoint() {
        val filename = "a12123d.awe"
        val extension = getExtensionFromFileName(filename)
        assertEquals(".awe", extension)
    }

    @Test
    fun getCorrectExtensionFromFilenameWithMultiplePoints() {
        val filename = "a121.2.23r%^%.3d.awe"
        val extension = getExtensionFromFileName(filename)
        assertEquals(".awe", extension)
    }

    @Test
    fun getEmptyStringFromFilenameWithNoPoints() {
        val filename = "a121223r%^%3dawe"
        val extension = getExtensionFromFileName(filename)
        assertEquals("", extension)
    }

    @Test
    fun getEmptyStringFromEmptyFilename() {
        val filename = ""
        val extension = getExtensionFromFileName(filename)
        assertEquals("", extension)
    }
}