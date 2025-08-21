package com.focus.app.domain.repository

import com.focus.app.domain.model.*
import kotlinx.coroutines.flow.Flow

interface UsageRepository {
    suspend fun saveUsageRecord(record: UsageRecord)
    suspend fun getUsageRecords(startTime: Long, endTime: Long): List<UsageRecord>
    fun getTodayUsage(): Flow<Long>
    fun getPlatformUsage(): Flow<Map<String, Long>>
    fun getUsageStats(): Flow<UsageStats>
    suspend fun deleteUsageRecord(id: Long)
    suspend fun clearUsageRecords()
    suspend fun refreshUsageData()
}

interface SearchRepository {
    suspend fun saveSearchHistory(history: SearchHistory)
    suspend fun getSearchHistory(): List<SearchHistory>
    suspend fun deleteSearchHistory(id: Long)
    suspend fun clearSearchHistory()
    suspend fun performSearch(keyword: String, platform: String): SearchResult
    suspend fun getSearchUrl(keyword: String, platform: String): String
}

interface SettingsRepository {
    suspend fun getSettings(): UserSettings
    suspend fun updateSettings(settings: UserSettings)
    suspend fun updateReminderInterval(interval: Long)
    suspend fun updateReminderMethods(methods: Set<ReminderMethod>)
    suspend fun updateDefaultPlatform(platform: String)
    suspend fun setServiceEnabled(enabled: Boolean)
    fun getSettingsFlow(): Flow<UserSettings>
}

interface PlatformRepository {
    suspend fun getPlatforms(): List<Platform>
    suspend fun getPlatform(id: String): Platform?
    suspend fun addPlatform(platform: Platform)
    suspend fun updatePlatform(platform: Platform)
    suspend fun deletePlatform(id: String)
    suspend fun enablePlatform(id: String, enabled: Boolean)
    fun getPlatformsFlow(): Flow<List<Platform>>
}

interface ReminderRepository {
    suspend fun saveReminder(reminder: ReminderInfo)
    suspend fun getReminders(): List<ReminderInfo>
    suspend fun deleteReminder(id: Long)
    suspend fun clearReminders()
    fun shouldShowReminder(appName: String, currentUsage: Long): Boolean
    suspend fun setReminderDismissedUntil(until: Long)
    suspend fun getReminderDismissedUntil(): Long
}