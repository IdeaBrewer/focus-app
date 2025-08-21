package com.focus.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focus.app.domain.model.*
import com.focus.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getPlatformsUseCase: GetPlatformsUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase,
    private val performSearchUseCase: PerformSearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val platforms = getPlatformsUseCase()
            val searchHistory = getSearchHistoryUseCase()
            
            _uiState.value = _uiState.value.copy(
                platforms = platforms,
                searchHistory = searchHistory,
                isLoading = false,
                selectedPlatform = platforms.firstOrNull { it.id == "xiaohongshu" }?.id ?: ""
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onPlatformSelected(platformId: String) {
        _uiState.value = _uiState.value.copy(selectedPlatform = platformId)
    }

    fun onSearch() {
        viewModelScope.launch {
            if (_uiState.value.searchQuery.isBlank()) return
            
            _uiState.value = _uiState.value.copy(isSearching = true)
            
            try {
                val result = performSearchUseCase(
                    _uiState.value.searchQuery,
                    _uiState.value.selectedPlatform
                )
                
                // Save search history
                val history = SearchHistory(
                    keyword = _uiState.value.searchQuery,
                    platform = _uiState.value.selectedPlatform,
                    searchTime = System.currentTimeMillis()
                )
                saveSearchHistoryUseCase(history)
                
                _uiState.value = _uiState.value.copy(
                    searchResult = result,
                    isSearching = false,
                    searchQuery = ""
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    searchResult = SearchResult(
                        keyword = _uiState.value.searchQuery,
                        platform = _uiState.value.selectedPlatform,
                        url = "",
                        isSuccess = false,
                        errorMessage = e.message
                    ),
                    isSearching = false
                )
            }
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(searchHistory = emptyList())
        }
    }

    fun onHistoryItemClick(keyword: String, platform: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = keyword,
            selectedPlatform = platform
        )
        onSearch()
    }
}

data class SearchUiState(
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val searchQuery: String = "",
    val selectedPlatform: String = "",
    val platforms: List<Platform> = emptyList(),
    val searchHistory: List<SearchHistory> = emptyList(),
    val searchResult: SearchResult? = null
)