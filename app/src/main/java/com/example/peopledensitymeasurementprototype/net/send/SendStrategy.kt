package com.example.peopledensitymeasurementprototype.net.send

import com.example.peopledensitymeasurementprototype.model.proto.Definitions

interface SendStrategy {
    fun sendMessage(data: Definitions.LocationMessageWrapper)
    fun sendSingleLocationData(data: Definitions.SingleLocationData)
    fun sendDensityMap(data: Definitions.DensityMap)
}
