package com.example.peopledensitymeasurementprototype.density

data class UTMArea (var nMin: Int = 0, var nMax: Int = 0, var eMin: Int = 0, var eMax: Int = 0) {
    fun clear() {
        eMax = 0
        eMin = 0
        nMin = 0
        nMax = 0
    }

    fun extend(location: UTMLocation) {
        if (location.northing > nMax) nMax = location.northing
        if (location.northing < nMin || nMin == 0) nMin = location.northing
        if (location.easting > eMax) eMax = location.easting
        if (location.easting < eMin || eMin == 0) eMin = location.easting
    }

    val northingRange get() = nMin..nMax
    val eastingRange get() = eMin..eMax
}
