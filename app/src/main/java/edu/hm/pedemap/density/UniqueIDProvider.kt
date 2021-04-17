package edu.hm.pedemap.density

import java.util.concurrent.ThreadLocalRandom

object UniqueIDProvider {

    /**
     * Generate a unique device-id
     */
    fun generateUniqueDeviceId(): Int {
        // TODO: This is very bad and has to be improved ASAP!
        return ThreadLocalRandom.current().nextInt()
    }
}
