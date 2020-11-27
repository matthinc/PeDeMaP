package com.example.peopledensitymeasurementprototype.density

import java.util.concurrent.ThreadLocalRandom

object UniqueIDProvider {

    fun generateUniqueDeviceId(): Int {
        //TODO: This is very bad and has to be improved ASAP!
        return ThreadLocalRandom.current().nextInt()
    }

}
