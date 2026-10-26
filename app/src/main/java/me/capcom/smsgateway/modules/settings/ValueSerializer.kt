package me.capcom.smsgateway.modules.settings

import com.google.gson.GsonBuilder
import java.lang.reflect.Type

class ValueSerializer {
    private val gson = GsonBuilder().create()

    fun <T> serialize(value: T): String = gson.toJson(value)

    fun <T> deserialize(value: String, typeOfT: Type): T = gson.fromJson(value, typeOfT)
}
