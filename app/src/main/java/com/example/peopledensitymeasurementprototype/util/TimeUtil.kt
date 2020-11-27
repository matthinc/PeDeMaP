package com.example.peopledensitymeasurementprototype.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Date.formatForLog(): String {
    fun Int.pad2() = toString().padStart(2, '0')

    return time.toString()
}

fun epochSecondTimestamp() = Date().time
