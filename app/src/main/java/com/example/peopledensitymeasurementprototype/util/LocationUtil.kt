package com.example.peopledensitymeasurementprototype.util

import android.location.Location
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity
import org.osmdroid.util.GeoPoint

fun LocationEntity.toGeoPoint() = GeoPoint(latitude, longitude)

