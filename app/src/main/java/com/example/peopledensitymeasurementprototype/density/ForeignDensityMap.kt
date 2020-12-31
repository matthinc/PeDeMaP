package com.example.peopledensitymeasurementprototype.density

import com.example.peopledensitymeasurementprototype.model.proto.Definitions

class ForeignDensityMap {

    var senderDeviceId: Int = 0
        private set

    var locations: List<UTMLocation> = emptyList()
        private set

    class Builder {

        private val foreignDensityMap = ForeignDensityMap()

        fun build(): ForeignDensityMap = foreignDensityMap

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
