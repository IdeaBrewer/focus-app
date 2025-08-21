package com.focus.app.data.database

import androidx.room.TypeConverter
import com.focus.app.domain.model.ReminderMethod

class Converters {
    @TypeConverter
    fun fromReminderMethodSet(methods: Set<ReminderMethod>): String {
        return methods.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toReminderMethodSet(methodsString: String): Set<ReminderMethod> {
        return if (methodsString.isEmpty()) {
            emptySet()
        } else {
            methodsString.split(",")
                .map { ReminderMethod.valueOf(it) }
                .toSet()
        }
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, Long>): String {
        return map.entries.joinToString(";") { "${it.key}=${it.value}" }
    }

    @TypeConverter
    fun toStringMap(mapString: String): Map<String, Long> {
        return if (mapString.isEmpty()) {
            emptyMap()
        } else {
            mapString.split(";")
                .map { it.split("=") }
                .associate { it[0] to it[1].toLong() }
        }
    }
}