package com.focus.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.focus.app.domain.model.*

@Database(
    entities = [
        UsageRecord::class,
        SearchHistory::class,
        UserSettings::class,
        Platform::class,
        ReminderInfo::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FocusDatabase : RoomDatabase() {
    abstract fun usageDao(): UsageDao
    abstract fun searchDao(): SearchDao
    abstract fun settingsDao(): SettingsDao
    abstract fun platformDao(): PlatformDao
    abstract fun reminderDao(): ReminderDao
}