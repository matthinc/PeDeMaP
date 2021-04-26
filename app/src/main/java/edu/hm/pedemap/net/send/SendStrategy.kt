package edu.hm.pedemap.net.send

import edu.hm.pedemap.model.proto.Definitions

interface SendStrategy {
    /**
     * Sends a message of type [Definitions.LocationMessageWrapper] to other devices.
     */
    fun sendMessage(data: Definitions.LocationMessageWrapper)

    /**
     * Sends a message of type [Definitions.SingleLocationData] to other devices.
     */
    fun sendSingleLocationData(data: Definitions.SingleLocationData)

    /**
     * Sends a message of type [Definitions.DensityMap] to other devices.
     */
    fun sendDensityMap(data: Definitions.DensityMap)

    /**
     * Sends a message of type [Definitions.WarnMessage] to other devices.
     */
    fun sendWarnMessage(data: Definitions.WarnMessage)
}
