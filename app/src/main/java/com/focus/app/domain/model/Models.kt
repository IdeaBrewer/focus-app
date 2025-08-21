package com.focus.app.domain.model

data class UsageRecord(
    val id: Long = 0,
    val appName: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long,
    val createdAt: Long = System.currentTimeMillis()
)

data class SearchHistory(
    val id: Long = 0,
    val keyword: String,
    val platform: String,
    val searchTime: Long,
    val createdAt: Long = System.currentTimeMillis()
)

data class UserSettings(
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

data class Platform(
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

data class ReminderInfo(
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