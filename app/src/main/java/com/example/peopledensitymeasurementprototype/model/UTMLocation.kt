package com.example.peopledensitymeasurementprototype.model

import android.location.Location
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity
import com.example.peopledensitymeasurementprototype.util.epochSecondTimestamp
import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.geom.Angle
import gov.nasa.worldwind.geom.coords.UTMCoord

data class GridLocation (
    val zoneId: Int,
    val northing: Long,
    val easting: Long,
    val northernHemisphere: Boolean
) {

    private val utmCoord by lazy { UTMCoord.fromUTM(zoneId, hemisphere, easting.toDouble(), northing.toDouble()) }

    val hemisphere get() = if (northernHemisphere) AVKey.NORTH else AVKey.SOUTH

    val latitude get() = utmCoord.latitude.degrees
    val longitude get() = utmCoord.longitude.degrees

    fun withOffset(n: Int, e: Int): GridLocation {
        return GridLocation(zoneId, northing + n, easting + e, northernHemisphere)
    }

    override fun toString(): String {
        val hemisphereLetter = if (northernHemisphere) "N" else "S"
        return "($zoneId$hemisphereLetter $northing $easting)"
    }
}

fun Location.toGridLocation(): GridLocation {
    val coord = UTMCoord.fromLatLon(Angle.fromDegreesLatitude(latitude), Angle.fromDegreesLongitude(longitude))

    val cellNorthing = (Math.floor(coord.northing) - Math.floor(coord.northing) % CELL_SIZE).toLong()
    val cellEasting = (Math.floor(coord.easting) - Math.floor(coord.easting) % CELL_SIZE).toLong()

    return GridLocation(
        coord.zone,
        cellNorthing,
        cellEasting,
        (coord.hemisphere == AVKey.NORTH)
    )
}

fun CombinedLocation.toLocationEntity() = LocationEntity(
    0,
    location.latitude,
    location.longitude,
    location.accuracy,
    epochSecondTimestamp(),
    gridLocation.zoneId,
    gridLocation.northing,
    gridLocation.easting,
    gridLocation.northernHemisphere
)

const val CELL_SIZE = 5

data class CombinedLocation(val location: Location, val gridLocation: GridLocation)

