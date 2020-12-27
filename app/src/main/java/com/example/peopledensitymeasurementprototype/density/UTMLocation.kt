package com.example.peopledensitymeasurementprototype.density

import android.location.Location
import com.example.peopledensitymeasurementprototype.model.entity.LocationEntity
import com.example.peopledensitymeasurementprototype.model.proto.Single
import com.example.peopledensitymeasurementprototype.util.epochSecondTimestamp
import com.example.peopledensitymeasurementprototype.util.sqr
import com.example.peopledensitymeasurementprototype.util.sqrt
import gov.nasa.worldwind.avlist.AVKey
import gov.nasa.worldwind.geom.Angle
import gov.nasa.worldwind.geom.coords.UTMCoord
import java.lang.IllegalStateException
import kotlin.math.cos
import kotlin.math.sin

class UTMLocation internal constructor () {

    var zoneId: Int = 0
        private set

    var northing : Int = 0
        private set

    var easting : Int = 0
        private set

    var northernHemisphere: Boolean = true
        private set

    var accuracy: Float? = null
        private set

    var preciseLat: Double? = null
        private set

    var preciseLon: Double? = null
        private set

    var bearing: Int? = null
        private set

    var speed: Int? = null
        private set

    var deviceId: Int? = null
        private set

    var timestamp: Long? = null
        private set

    var ttl: Int? = null
        private set

    private var latitude: Double? = null
    private var longitude: Double? = null

    fun hemisphere(): String {
        return if (northernHemisphere) AVKey.NORTH else AVKey.SOUTH
    }

    fun latitude(): Double {
        // Cache for performance
        if (latitude == null) latitude = UTMCoord.fromUTM(zoneId, hemisphere(), easting.toDouble(), northing.toDouble()).latitude.degrees
        return latitude!!
    }

    fun longitude(): Double {
        // Cache for performance
        if (longitude == null) longitude = UTMCoord.fromUTM(zoneId, hemisphere(), easting.toDouble(), northing.toDouble()).longitude.degrees
        return longitude!!
    }

    fun withOffset(n: Int, e: Int): UTMLocation {
        return UTMLocation().also {
            it.zoneId = zoneId
            it.northing = northing + n
            it.easting = easting + e
            it.northernHemisphere = northernHemisphere
        }
    }

    fun matchesPosition(other: UTMLocation): Boolean {
        return zoneId == other.zoneId &&
            northernHemisphere == other.northernHemisphere &&
            easting == other.easting &&
            northing == other.northing
    }

    fun distanceTo(other: UTMLocation): Int? {
        if (other.zoneId != zoneId) return null
        return sqrt(sqr(northing - other.northing) + sqr(easting - other.easting))
    }

    fun getDirection(): Direction {
        if (bearing == null) {
            throw IllegalStateException("Bearing is null!")
        }

        val bearingRad = (bearing!! / 360.0) * 2 * Math.PI

        return Direction(sin(bearingRad), cos(bearingRad))
    }

    override fun toString(): String {
        return "($zoneId $northing $easting)"
    }

    data class Direction(val north: Double, val east: Double)

    companion object {

        fun newBuilder(zone: Int, northing: Int, easting: Int, northernHermisphere: Boolean): Builder {
            return Builder(
                zone,
                northing,
                easting,
                northernHermisphere
            )
        }

        fun builderFromLocation(location: Location, cellSize: Int): Builder {
            val coord = UTMCoord.fromLatLon(Angle.fromDegreesLatitude(location.latitude), Angle.fromDegreesLongitude(location.longitude))

            // Align to grid
            val cellNorthing = (Math.floor(coord.northing) - Math.floor(coord.northing) % cellSize).toInt()
            val cellEasting = (Math.floor(coord.easting) - Math.floor(coord.easting) % cellSize).toInt()

            return newBuilder(
                coord.zone,
                cellNorthing,
                cellEasting,
                (coord.hemisphere == AVKey.NORTH)
            )
                .withBearing(location.bearing.toInt())
                .withAccuracy(location.accuracy)
                .withPreciseCoordinates(location.latitude, location.longitude)
                .withSpeed(location.speed.toInt())
        }

        fun builderFromSingleLocationProtobufObject(singleLocationData: Single.SingleLocationData): Builder {
            return newBuilder(
                singleLocationData.zoneId,
                singleLocationData.northing,
                singleLocationData.easting,
                singleLocationData.hemisphere
            )
                .withAccuracy(singleLocationData.accuracy)
                .withBearing(singleLocationData.bearing)
                .withSpeed(singleLocationData.speed)
                .withDeviceId(singleLocationData.deviceId)
                .withTimestamp(singleLocationData.timestamp)
                .withTTL(singleLocationData.ttl)
        }

        class Builder(val zoneId: Int, val northing: Int, val easting: Int, val northernHermisphere: Boolean) {
            private val utmLocationObject: UTMLocation =
                UTMLocation()

            init {
                utmLocationObject.northing = northing
                utmLocationObject.easting = easting
                utmLocationObject.zoneId = zoneId
                utmLocationObject.northernHemisphere = northernHermisphere
            }

            fun withAccuracy(accuracy: Float): Builder {
                utmLocationObject.accuracy = accuracy
                return this
            }

            fun withPreciseCoordinates(lat: Double, lon: Double): Builder {
                utmLocationObject.preciseLat = lat
                utmLocationObject.preciseLon = lon
                return this
            }

            fun withBearing(bearing: Int): Builder {
                utmLocationObject.bearing = bearing
                return this
            }

            fun withSpeed(speed: Int): Builder {
                utmLocationObject.speed = speed
                return this
            }

            fun withDeviceId(deviceId: Int): Builder {
                utmLocationObject.deviceId = deviceId
                return this
            }

            fun withTimestamp(timestamp: Long): Builder {
                utmLocationObject.timestamp = timestamp
                return this
            }

            fun withTTL(ttl: Int): Builder {
                utmLocationObject.ttl = ttl
                return this
            }

            fun build(): UTMLocation {
                return utmLocationObject
            }

        }
    }
}

fun UTMLocation.toLocationEntity() : LocationEntity {
    if (preciseLat == null || preciseLon == null) {
        throw IllegalStateException("Precise location is null!")
    }
    if (accuracy == null) {
        throw IllegalStateException("Accuracy is null!")
    }
    val tst = epochSecondTimestamp()

    return LocationEntity(
        0,
        this.preciseLat!!,
        this.preciseLon!!,
        this.accuracy!!,
        tst,
        this.zoneId,
        this.northing.toLong(),
        this.easting.toLong(),
        this.northernHemisphere
    )
}

fun UTMLocation.toSingleProto(): Single.SingleLocationData {
    if (accuracy == null) {
        throw IllegalStateException("Accuracy is null!")
    }
    if (bearing == null) {
        throw IllegalStateException("Bearing is null!")
    }
    if (deviceId == null) {
        throw IllegalStateException("DeviceId location is null!")
    }
    if (timestamp == null) {
        throw IllegalStateException("Timestamp location is null!")
    }
    if (speed == null) {
        throw IllegalStateException("Speed location is null!")
    }

    val proto = Single.SingleLocationData.newBuilder().apply {
        accuracy = this@toSingleProto.accuracy!!
        bearing = this@toSingleProto.bearing!!.toInt()
        deviceId = this@toSingleProto.deviceId!!
        northing = this@toSingleProto.northing
        easting = this@toSingleProto.easting
        zoneId = this@toSingleProto.zoneId
        hemisphere = this@toSingleProto.northernHemisphere
        timestamp = this@toSingleProto.timestamp!!
        speed = this@toSingleProto.speed!!.toInt()
        ttl = this@toSingleProto.ttl!!
    }

    return proto.build()
}




