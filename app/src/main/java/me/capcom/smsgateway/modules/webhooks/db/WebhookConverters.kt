package me.capcom.smsgateway.modules.webhooks.db

import androidx.room.TypeConverter

/**
 * Room type converters for webhook queue entities.
 */
class WebhookConverters {
    @TypeConverter
    fun fromStatus(status: WebhookStatus): String = status.value

    @TypeConverter
    fun toStatus(value: String): WebhookStatus = WebhookStatus.fromValue(value) ?: WebhookStatus.PENDING
}
