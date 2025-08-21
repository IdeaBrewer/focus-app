package com.focus.app.data.repository

import com.focus.app.data.database.FocusDao
import com.focus.app.data.service.UsageTrackerService
import com.focus.app.domain.model.*
import com.focus.app.domain.repository.UsageRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageRepositoryImpl @Inject constructor(
    private val usageDao: FocusDao.UsageDao,
    private val usageTrackerService: UsageTrackerService
) : UsageRepository {

    override suspend fun saveUsageRecord(record: UsageRecord) {
        usageDao.insertUsageRecord(record)
    }

    override suspend fun getUsageRecords(startTime: Long, endTime: Long): List<UsageRecord> {
        return usageDao.getUsageRecordsByTimeRange(startTime, endTime)
    }

    override fun getTodayUsage(): Flow<Long> {
        return usageDao.getTodayUsage()
    }

    override fun getPlatformUsage(): Flow<Map<String, Long>> {
        return usageDao.getTodayPlatformUsage()
    }

    override fun getUsageStats(): Flow<UsageStats> {
        return combine(
            getTodayUsage(),
            getPlatformUsage()
        ) { todayUsage, platformUsage ->
            UsageStats(
                todayUsage = todayUsage,
                weekUsage = todayUsage * 7, // Simplified calculation
                monthUsage = todayUsage * 30, // Simplified calculation
                platformUsage = platformUsage
            )
        }
    }

    override suspend fun deleteUsageRecord(id: Long) {
        // Note: This would require modifying UsageRecord to have an ID field
        // For now, we'll implement a basic version
    }

    override suspend fun clearUsageRecords() {
        usageDao.clearAllUsageRecords()
    }

    override suspend fun refreshUsageData() {
        usageTrackerService.refreshUsageData()
    }
}