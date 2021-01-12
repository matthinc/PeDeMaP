package com.example.peopledensitymeasurementprototype.density

import com.example.peopledensitymeasurementprototype.model.proto.Definitions

class ForeignDensityMap {

    var senderDeviceId: Int = 0
        private set

    var locations: List<UTMLocation> = emptyList()
        private set

    fun toDensityMapProto(): Definitions.DensityMap {
        return Definitions.DensityMap.newBuilder().apply {
            addAllData(this@ForeignDensityMap.locations.map { it.toSingleProto() })
            senderDeviceId = this@ForeignDensityMap.senderDeviceId
        }.build()
    }

    class Builder {

        private val foreignDensityMap = ForeignDensityMap()

        fun build(): ForeignDensityMap = foreignDensityMap

        fun setSender(sender: Int): Builder {
            foreignDensityMap.senderDeviceId = sender
            return this
        }

        fun setLocations(locations: List<UTMLocation>): Builder {
            foreignDensityMap.locations = locations
            return this
        }

        companion object {

            fun fromProto(data: Definitions.DensityMap): Builder {
                val builder = Builder()
                builder.foreignDensityMap.senderDeviceId = data.senderDeviceId
                builder.foreignDensityMap.locations = data.dataList.map {
                    UTMLocation.builderFromSingleLocationProtobufObject(it).build()
                }
                return builder
            }
        }
    }
}
