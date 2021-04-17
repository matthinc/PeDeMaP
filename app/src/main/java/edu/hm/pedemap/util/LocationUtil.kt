package edu.hm.pedemap.util

import edu.hm.pedemap.model.entity.LocationEntity
import org.osmdroid.util.GeoPoint

fun LocationEntity.toGeoPoint() = GeoPoint(latitude, longitude)
