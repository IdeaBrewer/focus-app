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
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val updateReminderIntervalUseCase: UpdateReminderIntervalUseCase,
    private val updateReminderMethodsUseCase: UpdateReminderMethodsUseCase,
    private val updateDefaultPlatformUseCase: UpdateDefaultPlatformUseCase,
    private val setServiceEnabledUseCase: SetServiceEnabledUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            getSettingsUseCase().collect { settings ->
                _uiState.value = _uiState.value.copy(
                    settings = settings,
                    isLoading = false
                )
            }
        }
    }

    fun onReminderIntervalChanged(interval: Long) {
        viewModelScope.launch {
            updateReminderIntervalUseCase(interval)
            _uiState.value = _uiState.value.copy(
                settings = _uiState.value.settings.copy(reminderInterval = interval)
            )
        }
    }

    fun onReminderMethodToggled(method: ReminderMethod) {
        viewModelScope.launch {
            val currentMethods = _uiState.value.settings.reminderMethods.toMutableSet()
            
            if (currentMethods.contains(method)) {
                currentMethods.remove(method)
            } else {
                currentMethods.add(method)
            }
            
            updateReminderMethodsUseCase(currentMethods)
            _uiState.value = _uiState.value.copy(
                settings = _uiState.value.settings.copy(reminderMethods = currentMethods)
            )
        }
    }

    fun onDefaultPlatformChanged(platform: String) {
        viewModelScope.launch {
            updateDefaultPlatformUseCase(platform)
            _uiState.value = _uiState.value.copy(
                settings = _uiState.value.settings.copy(defaultPlatform = platform)
            )
        }
    }

    fun onServiceEnabledChanged(enabled: Boolean) {
        viewModelScope.launch {
            setServiceEnabledUseCase(enabled)
            _uiState.value = _uiState.value.copy(
                settings = _uiState.value.settings.copy(isServiceEnabled = enabled)
            )
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            updateSettingsUseCase(_uiState.value.settings)
        }
    }

    fun refreshSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            loadData()
        }
    }
}

data class SettingsUiState(
    val isLoading: Boolean = true,
    val settings: UserSettings = UserSettings()
)