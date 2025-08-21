package com.focus.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.focus.app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.refreshSettings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.saveSettings() }) {
                        Icon(Icons.Default.Save, contentDescription = "保存")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingIndicator(modifier = Modifier.padding(paddingValues))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        ReminderSettingsCard(
                            settings = uiState.settings,
                            onIntervalChanged = { viewModel.onReminderIntervalChanged(it) },
                            onMethodToggled = { viewModel.onReminderMethodToggled(it) }
                        )
                    }
                    
                    item {
                        PlatformSettingsCard(
                            settings = uiState.settings,
                            onPlatformChanged = { viewModel.onDefaultPlatformChanged(it) }
                        )
                    }
                    
                    item {
                        ServiceSettingsCard(
                            settings = uiState.settings,
                            onServiceEnabledChanged = { viewModel.onServiceEnabledChanged(it) }
                        )
                    }
                    
                    item {
                        AboutCard()
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderSettingsCard(
    settings: com.focus.app.domain.model.UserSettings,
    onIntervalChanged: (Long) -> Unit,
    onMethodToggled: (com.focus.app.domain.model.ReminderMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "提醒设置",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Reminder Interval
            Column {
                Text(
                    text = "提醒间隔",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val intervals = listOf(
                        15 * 60 * 1000L to "15分钟",
                        30 * 60 * 1000L to "30分钟",
                        60 * 60 * 1000L to "1小时"
                    )
                    
                    intervals.forEach { (interval, label) ->
                        FilterChip(
                            selected = settings.reminderInterval == interval,
                            onClick = { onIntervalChanged(interval) },
                            label = { Text(label) }
                        )
                    }
                }
            }
            
            // Reminder Methods
            Column {
                Text(
                    text = "提醒方式",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    com.focus.app.domain.model.ReminderMethod.values().forEach { method ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (method) {
                                    com.focus.app.domain.model.ReminderMethod.POPUP -> "弹窗提醒"
                                    com.focus.app.domain.model.ReminderMethod.VIBRATION -> "震动提醒"
                                    com.focus.app.domain.model.ReminderMethod.SOUND -> "声音提醒"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            Switch(
                                checked = settings.reminderMethods.contains(method),
                                onCheckedChange = { onMethodToggled(method) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlatformSettingsCard(
    settings: com.focus.app.domain.model.UserSettings,
    onPlatformChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "平台设置",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Column {
                Text(
                    text = "默认搜索平台",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                val platforms = listOf(
                    "xiaohongshu" to "小红书",
                    "douyin" to "抖音",
                    "bilibili" to "B站"
                )
                
                platforms.forEach { (id, name) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        RadioButton(
                            selected = settings.defaultPlatform == id,
                            onClick = { onPlatformChanged(id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceSettingsCard(
    settings: com.focus.app.domain.model.UserSettings,
    onServiceEnabledChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "服务设置",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "启用监控服务",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = "需要权限支持才能正常工作",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Switch(
                    checked = settings.isServiceEnabled,
                    onCheckedChange = { onServiceEnabledChanged(it) }
                )
            }
        }
    }
}

@Composable
fun AboutCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "关于",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "版本",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "1.0.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "开发者",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Focus Team",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}