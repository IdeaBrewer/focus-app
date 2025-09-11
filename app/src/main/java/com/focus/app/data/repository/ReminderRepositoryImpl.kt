package com.focus.app.data.repository

import com.focus.app.data.database.ReminderDao
import com.focus.app.domain.model.*
import com.focus.app.domain.repository.ReminderRepository
import com.focus.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao,
    private val settingsRepository: SettingsRepository
) : ReminderRepository {

    private var reminderDismissedUntil: Long = 0

    override suspend fun saveReminder(reminder: ReminderInfo) {
        reminderDao.insertReminder(reminder)
    }

    override suspend fun getReminders(): List<ReminderInfo> {
        return reminderDao.getAllReminders().first()
    }

    override suspend fun deleteReminder(id: Long) {
        // Note: This would require modifying ReminderInfo to have an ID field
        // For now, we'll implement a basic version
    }

    override suspend fun clearReminders() {
        reminderDao.clearAllReminders()
    }

    override fun shouldShowReminder(appName: String, currentUsage: Long): Boolean {
        // Check if reminders are dismissed
        if (System.currentTimeMillis() < reminderDismissedUntil) {
            return false
        }

        // Check if usage exceeds threshold
        val threshold = 30 * 60 * 1000L // 30 minutes default
        return currentUsage >= threshold
    }

    override suspend fun setReminderDismissedUntil(until: Long) {
        reminderDismissedUntil = until
    }

    override suspend fun getReminderDismissedUntil(): Long {
        return reminderDismissedUntil
    }
}