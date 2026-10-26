package me.capcom.smsgateway.modules.messages.data

import java.util.Date
import me.capcom.smsgateway.domain.MessageContent

data class Message(
    val id: String,
    val content: MessageContent,
    val phoneNumbers: List<String>,

    val isEncrypted: Boolean,

    val createdAt: Date
)
