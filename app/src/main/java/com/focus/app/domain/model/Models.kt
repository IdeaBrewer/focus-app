package com.focus.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "usage_records")
data class UsageRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val appName: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String,
    val platform: String,
    val searchTime: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_settings")
@TypeConverters(Converters::class)
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderInterval: Long = 30 * 60 * 1000L, // 30 minutes
    val reminderMethods: Set<ReminderMethod> = setOf(ReminderMethod.POPUP),
    val defaultPlatform: String = "xiaohongshu",
    val isServiceEnabled: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class ReminderMethod {
    POPUP,
    VIBRATION,
    SOUND
}

@Entity(tableName = "platforms")
data class Platform(
    @PrimaryKey
    val id: String,
    val name: String,
    val packageName: String,
    val icon: Int,
    val isEnabled: Boolean = true,
    val searchUrlTemplate: String
)

data class UsageStats(
    val todayUsage: Long,
    val weekUsage: Long,
    val monthUsage: Long,
    val platformUsage: Map<String, Long>
)

@Entity(tableName = "reminders")
data class ReminderInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val appName: String,
    val usageTime: Long,
    val threshold: Long,
    val timestamp: Long
)

data class SearchResult(
    val keyword: String,
    val platform: String,
    val url: String,
    val isSuccess: Boolean,
    val errorMessage: String? = null
)

// Room Type Converters
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
}