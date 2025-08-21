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
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.shouldShowReminder) {
        if (uiState.shouldShowReminder) {
            // Show reminder overlay (to be implemented)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToSearch,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Search, contentDescription = "搜索")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            UsageStatsCard(
                                todayUsage = uiState.todayUsage,
                                platformUsage = uiState.platformUsage,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "快速操作",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = { viewModel.refreshData() },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Refresh, contentDescription = null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("刷新")
                                        }
                                        
                                        OutlinedButton(
                                            onClick = onNavigateToSearch,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Search, contentDescription = null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("搜索")
                                        }
                                    }
                                }
                            }
                        }
                        
                        item {
                            val recentApps = uiState.platformUsage.entries
                                .sortedByDescending { it.value }
                                .take(3)
                            
                            if (recentApps.isNotEmpty()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            text = "最近使用",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        
                                        recentApps.forEach { (app, duration) ->
                                            UsageStatItem(
                                                platformName = getPlatformName(app),
                                                duration = duration
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}