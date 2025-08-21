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
class MainViewModel @Inject constructor(
    private val getTodayUsageUseCase: GetTodayUsageUseCase,
    private val getPlatformUsageUseCase: GetPlatformUsageUseCase,
    private val getUsageStatsUseCase: GetUsageStatsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val checkReminderUseCase: CheckReminderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getTodayUsageUseCase(),
                getPlatformUsageUseCase(),
                getUsageStatsUseCase(),
                getSettingsUseCase()
            ) { todayUsage, platformUsage, usageStats, settings ->
                _uiState.value = _uiState.value.copy(
                    todayUsage = todayUsage,
                    platformUsage = platformUsage,
                    usageStats = usageStats,
                    settings = settings,
                    isLoading = false
                )
            }.collect()
        }
    }

    fun checkReminder(appName: String) {
        viewModelScope.launch {
            val shouldShow = checkReminderUseCase(appName, _uiState.value.todayUsage)
            _uiState.value = _uiState.value.copy(
                shouldShowReminder = shouldShow
            )
        }
    }

    fun dismissReminder() {
        _uiState.value = _uiState.value.copy(
            shouldShowReminder = false
        )
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            loadData()
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = true,
    val todayUsage: Long = 0,
    val platformUsage: Map<String, Long> = emptyMap(),
    val usageStats: UsageStats = UsageStats(0, 0, 0, emptyMap()),
    val settings: UserSettings = UserSettings(),
    val shouldShowReminder: Boolean = false
)