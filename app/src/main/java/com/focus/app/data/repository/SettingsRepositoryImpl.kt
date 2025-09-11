package com.focus.app.data.repository

import com.focus.app.data.database.SettingsDao
import com.focus.app.domain.model.*
import com.focus.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
) : SettingsRepository {

    override suspend fun getSettings(): UserSettings {
        return settingsDao.getSettings().first() ?: UserSettings()
    }

    override suspend fun updateSettings(settings: UserSettings) {
        settingsDao.updateSettings(settings.copy(lastUpdated = System.currentTimeMillis()))
    }

    override suspend fun updateReminderInterval(interval: Long) {
        val currentSettings = getSettings()
        updateSettings(currentSettings.copy(reminderInterval = interval))
    }

    override suspend fun updateReminderMethods(methods: Set<ReminderMethod>) {
        val currentSettings = getSettings()
        updateSettings(currentSettings.copy(reminderMethods = methods))
    }

    override suspend fun updateDefaultPlatform(platform: String) {
        val currentSettings = getSettings()
        updateSettings(currentSettings.copy(defaultPlatform = platform))
    }

    override suspend fun setServiceEnabled(enabled: Boolean) {
        val currentSettings = getSettings()
        updateSettings(currentSettings.copy(isServiceEnabled = enabled))
    }

    override fun getSettingsFlow(): Flow<UserSettings> {
        return settingsDao.getSettings().map { it ?: UserSettings() }
    }
}