package com.example.peopledensitymeasurementprototype.util

import android.location.Location

fun Location.formatForLog() = "$latitude, $longitude ($accuracy m)"
