package com.example.peopledensitymeasurementprototype.util

import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.formatForLog(): String {
    fun Int.pad2() = toString().padStart(2, '0')

    return "${dayOfMonth.pad2()}.${monthValue.pad2()}.${year.pad2()}" +
        " ${hour.pad2()}:${minute.pad2()}:${second.pad2()}"
}

fun epochSecondTimestamp() = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
