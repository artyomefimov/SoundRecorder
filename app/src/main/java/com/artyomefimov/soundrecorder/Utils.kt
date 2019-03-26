package com.artyomefimov.soundrecorder

import java.util.*

fun getNameAccordingToDate(): String {
    val calendar: Calendar = Calendar.getInstance()
    return "${calendar[Calendar.DAY_OF_MONTH]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.YEAR]}" +
            "_${calendar[Calendar.HOUR_OF_DAY]}.${calendar[Calendar.MINUTE]}.${calendar[Calendar.SECOND]}.mp3"
}