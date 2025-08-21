package com.focus.app.di

import com.focus.app.domain.usecase.*
import com.focus.app.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetTodayUsageUseCase(
        usageRepository: UsageRepository
    ): GetTodayUsageUseCase {
        return GetTodayUsageUseCase(usageRepository)
    }

    @Provides
    @Singleton
    fun provideGetPlatformUsageUseCase(
        usageRepository: UsageRepository
    ): GetPlatformUsageUseCase {
        return GetPlatformUsageUseCase(usageRepository)
    }

    @Provides
    @Singleton
    fun provideGetUsageStatsUseCase(
        usageRepository: UsageRepository
    ): GetUsageStatsUseCase {
        return GetUsageStatsUseCase(usageRepository)
    }

    @Provides
    @Singleton
    fun provideSaveUsageRecordUseCase(
        usageRepository: UsageRepository
    ): SaveUsageRecordUseCase {
        return SaveUsageRecordUseCase(usageRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchHistoryUseCase(
        searchRepository: SearchRepository
    ): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCase(searchRepository)
    }

    @Provides
    @Singleton
    fun provideSaveSearchHistoryUseCase(
        searchRepository: SearchRepository
    ): SaveSearchHistoryUseCase {
        return SaveSearchHistoryUseCase(searchRepository)
    }

    @Provides
    @Singleton
    fun providePerformSearchUseCase(
        searchRepository: SearchRepository
    ): PerformSearchUseCase {
        return PerformSearchUseCase(searchRepository)
    }

    @Provides
    @Singleton
    fun provideGetSettingsUseCase(
        settingsRepository: SettingsRepository
    ): GetSettingsUseCase {
        return GetSettingsUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateSettingsUseCase(
        settingsRepository: SettingsRepository
    ): UpdateSettingsUseCase {
        return UpdateSettingsUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateReminderIntervalUseCase(
        settingsRepository: SettingsRepository
    ): UpdateReminderIntervalUseCase {
        return UpdateReminderIntervalUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateReminderMethodsUseCase(
        settingsRepository: SettingsRepository
    ): UpdateReminderMethodsUseCase {
        return UpdateReminderMethodsUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateDefaultPlatformUseCase(
        settingsRepository: SettingsRepository
    ): UpdateDefaultPlatformUseCase {
        return UpdateDefaultPlatformUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideSetServiceEnabledUseCase(
        settingsRepository: SettingsRepository
    ): SetServiceEnabledUseCase {
        return SetServiceEnabledUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideGetPlatformsUseCase(
        platformRepository: PlatformRepository
    ): GetPlatformsUseCase {
        return GetPlatformsUseCase(platformRepository)
    }

    @Provides
    @Singleton
    fun provideCheckReminderUseCase(
        reminderRepository: ReminderRepository,
        settingsRepository: SettingsRepository
    ): CheckReminderUseCase {
        return CheckReminderUseCase(reminderRepository, settingsRepository)
    }

    @Provides
    @Singleton
    fun provideSaveReminderUseCase(
        reminderRepository: ReminderRepository
    ): SaveReminderUseCase {
        return SaveReminderUseCase(reminderRepository)
    }

    @Provides
    @Singleton
    fun provideSetReminderDismissedUseCase(
        reminderRepository: ReminderRepository
    ): SetReminderDismissedUseCase {
        return SetReminderDismissedUseCase(reminderRepository)
    }
}