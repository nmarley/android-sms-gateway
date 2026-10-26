package me.capcom.smsgateway.domain

sealed class MessageContent {
    data class Text(val text: String) : MessageContent() {
        override fun toString(): String = text
    }

    data class Data(val data: String, val port: UShort) : MessageContent() {
        override fun toString(): String = "$data:$port"
    }

    data class Mms(val subject: String?, val text: String?, val attachments: List<Attachment>) : MessageContent() {
        data class Attachment(val contentType: String, val name: String?, val data: String)

        override fun toString(): String {
            val lines = mutableListOf<String>()

            subject?.let { lines.add(it) }
            text?.let { lines.add(it) }
            if (attachments.isNotEmpty()) {
                lines.add("Attachments: ${attachments.size}")
            }

            return lines.joinToString("\r\n")
        }
    }
}
