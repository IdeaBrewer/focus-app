package com.focus.app.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focus.app.data.repository.UsageRepository
import com.focus.app.data.repository.SettingsRepository
import com.focus.app.domain.model.Platform
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val todayUsage: Long = 0L,
    val platformUsage: Map<String, Long> = emptyMap(),
    val selectedPlatform: String = "xiaohongshu",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usageRepository: UsageRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
        observeUsageData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val settings = settingsRepository.getSettings()
                _uiState.value = _uiState.value.copy(
                    selectedPlatform = settings.defaultPlatform,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
    
    private fun observeUsageData() {
        viewModelScope.launch {
            usageRepository.todayUsage.collect { usage ->
                _uiState.value = _uiState.value.copy(todayUsage = usage)
            }
        }
        
        viewModelScope.launch {
            usageRepository.platformUsage.collect { usage ->
                _uiState.value = _uiState.value.copy(platformUsage = usage)
            }
        }
    }
    
    fun selectPlatform(platform: String) {
        _uiState.value = _uiState.value.copy(selectedPlatform = platform)
        
        viewModelScope.launch {
            try {
                settingsRepository.updateDefaultPlatform(platform)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun refreshData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                usageRepository.refreshUsageData()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}