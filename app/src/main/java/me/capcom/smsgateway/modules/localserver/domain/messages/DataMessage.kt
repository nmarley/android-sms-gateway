package me.capcom.smsgateway.modules.localserver.domain.messages

data class DataMessage(
    // Base64-encoded payload
    val data: String,
    // Destination port (0-65535)
    val port: Int
)
