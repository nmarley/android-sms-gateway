package me.capcom.smsgateway.modules.encryption.providers

import java.lang.reflect.Type
import me.capcom.smsgateway.modules.settings.KeyValueStorage

internal class FakeStorage(private val passphrase: String) : KeyValueStorage {
    override fun <T> set(key: String, value: T) {}
    override fun <T> get(key: String, typeOfT: Type): T? {
        @Suppress("UNCHECKED_CAST")
        return when (key) {
            "passphrase" -> passphrase as T
            else -> null
        }
    }

    override fun remove(key: String) {}
}
