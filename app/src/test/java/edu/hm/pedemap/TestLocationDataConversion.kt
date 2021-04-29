package edu.hm.pedemap

import android.location.Location
import edu.hm.pedemap.density.UTMLocation
import edu.hm.pedemap.density.toSingleProto
import edu.hm.pedemap.model.proto.Definitions
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class TestLocationDataConversion {

    @Test
    fun testConvertLocationToUTMLocation() {
        val location = Mockito.mock(Location::class.java)

        Mockito.`when`(location.latitude).thenReturn(48.1611543)
        Mockito.`when`(location.longitude).thenReturn(11.5900521)
        Mockito.`when`(location.accuracy).thenReturn(25f)

        val utmLocation = UTMLocation.builderFromLocation(location, 5).build()

        Assert.assertEquals(5337455, utmLocation.northing)
        Assert.assertEquals(692595, utmLocation.easting)
        Assert.assertTrue(utmLocation.northernHemisphere)
        Assert.assertEquals(32, utmLocation.zoneId)
        Assert.assertEquals(25f, utmLocation.accuracy)
    }

    @Test
    fun testProtobufSerialization() {
        // Serialize
        val utmLocation = UTMLocation.newBuilder(32, 5337455, 692595, true)
            .withDeviceId(1510)
            .withAccuracy(25f)
            .withTTL(100)
            .withBearing(180)
            .withPreciseCoordinates(48.1611543, 11.5900521)
            .withTimestamp(1000)
            .withSpeed(20)
            .build()

        val singleProto = utmLocation.toSingleProto()

        val wrapper = Definitions.LocationMessageWrapper.newBuilder().apply {
            single = singleProto
        }.build()

        val bytes = wrapper.toByteArray()

        // Deserialize
        val wrapper2 = Definitions.LocationMessageWrapper.parseFrom(bytes)

        Assert.assertTrue(wrapper2.hasSingle())

        val singleProto2 = wrapper2.single

        val utmLocation2 = UTMLocation.builderFromSingleLocationProtobufObject(singleProto2).build()

        Assert.assertEquals(utmLocation.zoneId, utmLocation2.zoneId)
        Assert.assertEquals(utmLocation.northing, utmLocation2.northing)
        Assert.assertEquals(utmLocation.easting, utmLocation2.easting)
        Assert.assertEquals(utmLocation.northernHemisphere, utmLocation2.northernHemisphere)
        Assert.assertEquals(utmLocation.deviceId, utmLocation2.deviceId)
        Assert.assertEquals(utmLocation.ttl, utmLocation2.ttl)
        Assert.assertEquals(utmLocation.bearing, utmLocation2.bearing)
        Assert.assertEquals(utmLocation.timestamp, utmLocation2.timestamp)
        Assert.assertEquals(utmLocation.speed, utmLocation2.speed)

        // Precise coordinates are not serialized
        Assert.assertNull(utmLocation2.preciseLat)
        Assert.assertNull(utmLocation2.preciseLon)
    }
}
