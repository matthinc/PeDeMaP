package com.example.peopledensitymeasurementprototype.messages

import com.example.peopledensitymeasurementprototype.model.proto.Definitions
import com.example.peopledensitymeasurementprototype.util.epochSecondTimestamp

data class WarnMessage(
    val message: String,
    val validUntil: Long,
    val latitude: Double,
    val longitude: Double
) {

    fun isValid(): Boolean = validUntil > epochSecondTimestamp()

    fun isInvalid() = !isValid()

    fun toProto(): Definitions.WarnMessage {
        return Definitions.WarnMessage.newBuilder().apply {
            message = this@WarnMessage.message
            validUntil = this@WarnMessage.validUntil
            latitude = this@WarnMessage.latitude
            longitude = this@WarnMessage.longitude
        }.build()
    }

    companion object {
        fun fromProto(proto: Definitions.WarnMessage): WarnMessage {
            return WarnMessage(proto.message, proto.validUntil, proto.latitude, proto.longitude)
        }
    }
}
