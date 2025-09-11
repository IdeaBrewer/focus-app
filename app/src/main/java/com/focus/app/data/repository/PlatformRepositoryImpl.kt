package com.focus.app.data.repository

import com.focus.app.data.database.PlatformDao
import com.focus.app.domain.model.*
import com.focus.app.domain.repository.PlatformRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlatformRepositoryImpl @Inject constructor(
    private val platformDao: PlatformDao
) : PlatformRepository {

    override suspend fun getPlatforms(): List<Platform> {
        return getDefaultPlatforms() + platformDao.getAllPlatforms().first()
    }

    override suspend fun getPlatform(id: String): Platform? {
        return getDefaultPlatforms().find { it.id == id } ?: platformDao.getPlatformById(id)
    }

    override suspend fun addPlatform(platform: Platform) {
        platformDao.insertPlatform(platform)
    }

    override suspend fun updatePlatform(platform: Platform) {
        platformDao.updatePlatform(platform)
    }

    override suspend fun deletePlatform(id: String) {
        getPlatform(id)?.let { platform ->
            platformDao.deletePlatform(platform)
        }
    }

    override suspend fun enablePlatform(id: String, enabled: Boolean) {
        platformDao.updatePlatformEnabled(id, enabled)
    }

    override fun getPlatformsFlow(): Flow<List<Platform>> {
        return platformDao.getAllPlatforms().map { dbPlatforms ->
            getDefaultPlatforms() + dbPlatforms
        }
    }

    private fun getDefaultPlatforms(): List<Platform> {
        return listOf(
            Platform(
                id = "xiaohongshu",
                name = "小红书",
                packageName = "com.xingin.xhs",
                icon = 0, // Will be replaced with actual resource ID
                isEnabled = true,
                searchUrlTemplate = "https://www.xiaohongshu.com/search_result?keyword={keyword}"
            ),
            Platform(
                id = "douyin",
                name = "抖音",
                packageName = "com.ss.android.ugc.aweme",
                icon = 0, // Will be replaced with actual resource ID
                isEnabled = true,
                searchUrlTemplate = "https://www.douyin.com/search/{keyword}"
            ),
            Platform(
                id = "bilibili",
                name = "B站",
                packageName = "tv.danmaku.bili",
                icon = 0, // Will be replaced with actual resource ID
                isEnabled = true,
                searchUrlTemplate = "https://search.bilibili.com/all?keyword={keyword}"
            )
        )
    }
}