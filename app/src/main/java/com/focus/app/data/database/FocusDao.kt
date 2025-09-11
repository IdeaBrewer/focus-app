package com.focus.app.data.database

import androidx.room.*
import androidx.room.ColumnInfo
import com.focus.app.domain.model.*
import kotlinx.coroutines.flow.Flow

data class PlatformUsage(
    @ColumnInfo(name = "appName") val appName: String,
    @ColumnInfo(name = "totalDuration") val totalDuration: Long
)

@Dao
interface UsageDao {
    @Query("SELECT * FROM usage_records ORDER BY startTime DESC")
    fun getAllUsageRecords(): Flow<List<UsageRecord>>

    @Query("SELECT * FROM usage_records WHERE startTime >= :startTime AND endTime <= :endTime")
    suspend fun getUsageRecordsByTimeRange(startTime: Long, endTime: Long): List<UsageRecord>

    @Query("SELECT SUM(duration) FROM usage_records WHERE DATE(startTime / 1000, 'unixepoch') = DATE('now')")
    fun getTodayUsage(): Flow<Long>

    @Query("SELECT appName, SUM(duration) as totalDuration FROM usage_records WHERE DATE(startTime / 1000, 'unixepoch') = DATE('now') GROUP BY appName")
    fun getTodayPlatformUsage(): Flow<List<PlatformUsage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsageRecord(record: UsageRecord): Long

    @Delete
    suspend fun deleteUsageRecord(record: UsageRecord)

    @Query("DELETE FROM usage_records")
    suspend fun clearAllUsageRecords()
}

@Dao
interface SearchDao {
    @Query("SELECT * FROM search_history ORDER BY searchTime DESC")
    fun getAllSearchHistory(): Flow<List<SearchHistory>>

    @Query("SELECT * FROM search_history ORDER BY searchTime DESC LIMIT 10")
    suspend fun getRecentSearchHistory(): List<SearchHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(history: SearchHistory): Long

    @Delete
    suspend fun deleteSearchHistory(history: SearchHistory)

    @Query("DELETE FROM search_history")
    suspend fun clearAllSearchHistory()
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM user_settings LIMIT 1")
    fun getSettings(): Flow<UserSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: UserSettings)

    @Update
    suspend fun updateSettings(settings: UserSettings)
}

@Dao
interface PlatformDao {
    @Query("SELECT * FROM platforms ORDER BY name")
    fun getAllPlatforms(): Flow<List<Platform>>

    @Query("SELECT * FROM platforms WHERE id = :platformId")
    suspend fun getPlatformById(platformId: String): Platform?

    @Query("SELECT * FROM platforms WHERE isEnabled = 1")
    fun getEnabledPlatforms(): Flow<List<Platform>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlatform(platform: Platform): Long

    @Update
    suspend fun updatePlatform(platform: Platform)

    @Delete
    suspend fun deletePlatform(platform: Platform)

    @Query("UPDATE platforms SET isEnabled = :enabled WHERE id = :platformId")
    suspend fun updatePlatformEnabled(platformId: String, enabled: Boolean)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY timestamp DESC")
    fun getAllReminders(): Flow<List<ReminderInfo>>

    @Query("SELECT * FROM reminders WHERE appName = :appName ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestReminderForApp(appName: String): ReminderInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderInfo): Long

    @Delete
    suspend fun deleteReminder(reminder: ReminderInfo)

    @Query("DELETE FROM reminders")
    suspend fun clearAllReminders()
}