package edu.hm.pedemap.messages

class MessageManager {
    private val messages = hashSetOf<WarnMessage>()

    var observer: (MessageManager) -> Unit = {}

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
