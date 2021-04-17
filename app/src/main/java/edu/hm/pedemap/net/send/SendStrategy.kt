package edu.hm.pedemap.net.send

import edu.hm.pedemap.model.proto.Definitions

interface SendStrategy {
    fun sendMessage(data: Definitions.LocationMessageWrapper)
    fun sendSingleLocationData(data: Definitions.SingleLocationData)
    fun sendDensityMap(data: Definitions.DensityMap)
    fun sendWarnMessage(data: Definitions.WarnMessage)
}
