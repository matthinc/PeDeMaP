package com.example.peopledensitymeasurementprototype.net.send

import com.example.peopledensitymeasurementprototype.model.proto.Single

interface SendStrategy {
    fun sendSingleLocationData(data: Single.SingleLocationData)
}
