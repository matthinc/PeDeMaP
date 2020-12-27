package com.example.peopledensitymeasurementprototype.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

fun Date.formatForLog(): String {
    fun Int.pad2() = toString().padStart(2, '0')
    return "${year + 1900}-${month.pad2()}-${day.pad2()} ${hours.pad2()}:${minutes.pad2()}:${seconds.pad2()}"
}

fun epochSecondTimestamp() = Date().time / 1_000
