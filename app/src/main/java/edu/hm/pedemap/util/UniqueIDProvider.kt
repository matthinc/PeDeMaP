package edu.hm.pedemap.util

import java.util.concurrent.ThreadLocalRandom

object UniqueIDProvider {

    /**
     * Generate a unique device-id
     */
    fun generateUniqueDeviceId(): Int {
        return ThreadLocalRandom.current().nextInt()
    }
}
