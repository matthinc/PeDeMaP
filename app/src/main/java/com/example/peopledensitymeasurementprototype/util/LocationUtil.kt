package com.example.peopledensitymeasurementprototype.util

import android.location.Location
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity
import org.osmdroid.util.GeoPoint

fun Location.formatForLog() = "$latitude, $longitude ($accuracy m)"

fun LocationEntity.toGeoPoint() = GeoPoint(latitude, longitude)

fun Location.toLocationEntity() = LocationEntity(0, latitude, longitude, accuracy, time)
