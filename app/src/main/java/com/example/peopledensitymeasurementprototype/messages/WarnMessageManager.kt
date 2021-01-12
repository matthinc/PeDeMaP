package com.example.peopledensitymeasurementprototype.messages

import java.util.*

class WarnMessageManager {
    private val messages = hashSetOf<WarnMessage>()

    var observer: (WarnMessageManager)->Unit = {}

    private fun removeInvalid() {
        messages.removeIf(WarnMessage::isInvalid)
    }

    fun add(message: WarnMessage) {
        messages.add(message)
        removeInvalid()

        observer(this)
    }

    fun getAll(): Set<WarnMessage> {
        removeInvalid()
        return messages
    }
}
