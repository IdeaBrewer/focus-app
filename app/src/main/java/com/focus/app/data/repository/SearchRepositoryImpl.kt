package com.focus.app.data.repository

import com.focus.app.data.database.SearchDao
import com.focus.app.domain.model.*
import com.focus.app.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val searchDao: SearchDao
) : SearchRepository {

    override suspend fun saveSearchHistory(history: SearchHistory) {
        searchDao.insertSearchHistory(history)
    }

    override suspend fun getSearchHistory(): List<SearchHistory> {
        return searchDao.getRecentSearchHistory()
    }

    override suspend fun deleteSearchHistory(id: Long) {
        // Note: This would require modifying SearchHistory to have an ID field
        // For now, we'll implement a basic version
    }

    override suspend fun clearSearchHistory() {
        searchDao.clearAllSearchHistory()
    }

    override suspend fun performSearch(keyword: String, platform: String): SearchResult {
        return try {
            val url = getSearchUrl(keyword, platform)
            SearchResult(
                keyword = keyword,
                platform = platform,
                url = url,
                isSuccess = true
            )
        } catch (e: Exception) {
            SearchResult(
                keyword = keyword,
                platform = platform,
                url = "",
                isSuccess = false,
                errorMessage = e.message
            )
        }
    }

    override suspend fun getSearchUrl(keyword: String, platform: String): String {
        return when (platform) {
            "xiaohongshu" -> "https://www.xiaohongshu.com/search_result?keyword=$keyword"
            "douyin" -> "https://www.douyin.com/search/$keyword"
            "bilibili" -> "https://search.bilibili.com/all?keyword=$keyword"
            else -> "https://www.google.com/search?q=$keyword"
        }
    }
}