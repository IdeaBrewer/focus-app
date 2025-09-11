package com.focus.app.di

import android.content.Context
import androidx.room.Room
import com.focus.app.data.database.*
import com.focus.app.data.repository.*
import com.focus.app.data.service.*
import com.focus.app.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUsageRepository(
        usageRepositoryImpl: UsageRepositoryImpl
    ): UsageRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindPlatformRepository(
        platformRepositoryImpl: PlatformRepositoryImpl
    ): PlatformRepository

    @Binds
    @Singleton
    abstract fun bindReminderRepository(
        reminderRepositoryImpl: ReminderRepositoryImpl
    ): ReminderRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFocusDatabase(@ApplicationContext context: Context): FocusDatabase {
        return Room.databaseBuilder(
            context,
            FocusDatabase::class.java,
            "focus_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUsageDao(database: FocusDatabase): UsageDao = database.usageDao()

    @Provides
    @Singleton
    fun provideSearchDao(database: FocusDatabase): SearchDao = database.searchDao()

    @Provides
    @Singleton
    fun provideSettingsDao(database: FocusDatabase): SettingsDao = database.settingsDao()

    @Provides
    @Singleton
    fun providePlatformDao(database: FocusDatabase): PlatformDao = database.platformDao()

    @Provides
    @Singleton
    fun provideReminderDao(database: FocusDatabase): ReminderDao = database.reminderDao()
}

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideUsageTrackerService(@ApplicationContext context: Context): UsageTrackerService {
        return UsageTrackerService(context)
    }

    @Provides
    @Singleton
    fun provideReminderManagerService(@ApplicationContext context: Context): ReminderManagerService {
        return ReminderManagerService(context)
    }

    @Provides
    @Singleton
    fun provideOverlayManagerService(@ApplicationContext context: Context): OverlayManagerService {
        return OverlayManagerService(context)
    }
}