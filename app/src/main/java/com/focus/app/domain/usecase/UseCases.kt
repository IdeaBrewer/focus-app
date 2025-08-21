package com.focus.app.domain.usecase

import com.focus.app.domain.model.*
import com.focus.app.domain.repository.*
import kotlinx.coroutines.flow.Flow

class GetTodayUsageUseCase(
    private val usageRepository: UsageRepository
) {
    operator fun invoke(): Flow<Long> = usageRepository.getTodayUsage()
}

class GetPlatformUsageUseCase(
    private val usageRepository: UsageRepository
) {
    operator fun invoke(): Flow<Map<String, Long>> = usageRepository.getPlatformUsage()
}

class GetUsageStatsUseCase(
    private val usageRepository: UsageRepository
) {
    operator fun invoke(): Flow<UsageStats> = usageRepository.getUsageStats()
}

class SaveUsageRecordUseCase(
    private val usageRepository: UsageRepository
) {
    suspend operator fun invoke(record: UsageRecord) {
        usageRepository.saveUsageRecord(record)
    }
}

class GetSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(): List<SearchHistory> = searchRepository.getSearchHistory()
}

class SaveSearchHistoryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(history: SearchHistory) {
        searchRepository.saveSearchHistory(history)
    }
}

class PerformSearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(keyword: String, platform: String): SearchResult {
        return searchRepository.performSearch(keyword, platform)
    }
}

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): UserSettings = settingsRepository.getSettings()
}

class UpdateSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(settings: UserSettings) {
        settingsRepository.updateSettings(settings)
    }
}

class UpdateReminderIntervalUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(interval: Long) {
        settingsRepository.updateReminderInterval(interval)
    }
}

class UpdateReminderMethodsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(methods: Set<ReminderMethod>) {
        settingsRepository.updateReminderMethods(methods)
    }
}

class UpdateDefaultPlatformUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(platform: String) {
        settingsRepository.updateDefaultPlatform(platform)
    }
}

class SetServiceEnabledUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        settingsRepository.setServiceEnabled(enabled)
    }
}

class GetPlatformsUseCase(
    private val platformRepository: PlatformRepository
) {
    suspend operator fun invoke(): List<Platform> = platformRepository.getPlatforms()
}

class CheckReminderUseCase(
    private val reminderRepository: ReminderRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(appName: String, currentUsage: Long): Boolean {
        return reminderRepository.shouldShowReminder(appName, currentUsage)
    }
}

class SaveReminderUseCase(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminder: ReminderInfo) {
        reminderRepository.saveReminder(reminder)
    }
}

class SetReminderDismissedUseCase(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(until: Long) {
        reminderRepository.setReminderDismissedUntil(until)
    }
}