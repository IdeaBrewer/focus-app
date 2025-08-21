package com.focus.app.data.service

import android.content.Context
import android.app.usage.UsageStatsManager
import android.app.usage.UsageStats
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UsageTrackerService(private val context: Context) {

    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    private val packageManager = context.packageManager

    suspend fun getCurrentUsage(): Map<String, Long> = withContext(Dispatchers.IO) {
        try {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - (24 * 60 * 60 * 1000) // Last 24 hours
            
            val usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )
            
            val appUsage = mutableMapOf<String, Long>()
            
            usageStats.forEach { stat ->
                if (stat.totalTimeInForeground > 0) {
                    val appName = getAppName(stat.packageName)
                    appUsage[appName] = stat.totalTimeInForeground
                }
            }
            
            appUsage
        } catch (e: Exception) {
            emptyMap()
        }
    }

    suspend fun getTodayUsage(): Long = withContext(Dispatchers.IO) {
        try {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            
            val startTime = calendar.timeInMillis
            val endTime = System.currentTimeMillis()
            
            val usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )
            
            usageStats.sumOf { it.totalTimeInForeground }
        } catch (e: Exception) {
            0L
        }
    }

    suspend fun getPlatformUsage(): Map<String, Long> = withContext(Dispatchers.IO) {
        val platformPackages = mapOf(
            "xiaohongshu" to "com.xingin.xhs",
            "douyin" to "com.ss.android.ugc.aweme",
            "bilibili" to "tv.danmaku.bili"
        )
        
        val platformUsage = mutableMapOf<String, Long>()
        
        platformPackages.forEach { (platform, packageName) ->
            val usage = getAppUsage(packageName)
            if (usage > 0) {
                platformUsage[platform] = usage
            }
        }
        
        platformUsage
    }

    private suspend fun getAppUsage(packageName: String): Long = withContext(Dispatchers.IO) {
        try {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - (24 * 60 * 60 * 1000)
            
            val usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )
            
            usageStats
                .filter { it.packageName == packageName }
                .sumOf { it.totalTimeInForeground }
        } catch (e: Exception) {
            0L
        }
    }

    private fun getAppName(packageName: String): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val applicationInfo = packageInfo.applicationInfo
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    suspend fun refreshUsageData() {
        // This method will be used to manually refresh usage data
        // Implementation will depend on specific requirements
    }

    fun hasUsageStatsPermission(): Boolean {
        try {
            val endTime = System.currentTimeMillis()
            val startTime = endTime - (24 * 60 * 60 * 1000)
            
            val usageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime
            )
            
            return usageStats.isNotEmpty()
        } catch (e: Exception) {
            return false
        }
    }
}