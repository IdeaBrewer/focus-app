package com.focus.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.focus.app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.searchResult) {
        uiState.searchResult?.let { result ->
            if (result.isSuccess) {
                // Open browser with search URL
                // This will be implemented with actual browser intent
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("搜索") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }
                else -> {
                    FocusSearchBar(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        onSearch = { viewModel.onSearch() },
                        enabled = !uiState.isSearching
                    )
                    
                    if (uiState.platforms.isNotEmpty()) {
                        PlatformSelector(
                            platforms = uiState.platforms,
                            selectedPlatform = uiState.selectedPlatform,
                            onPlatformSelected = { viewModel.onPlatformSelected(it) },
                            enabled = !uiState.isSearching
                        )
                    }
                    
                    if (uiState.isSearching) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    
                    if (uiState.searchResult != null) {
                        val result = uiState.searchResult!!
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (result.isSuccess)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (result.isSuccess) "搜索成功" else "搜索失败",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (result.isSuccess)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onErrorContainer
                                )
                                
                                Text(
                                    text = "关键词: ${result.keyword}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (result.isSuccess)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onErrorContainer
                                )
                                
                                Text(
                                    text = "平台: ${getPlatformName(result.platform)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (result.isSuccess)
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    else
                                        MaterialTheme.colorScheme.onErrorContainer
                                )
                                
                                if (result.errorMessage != null) {
                                    Text(
                                        text = "错误: ${result.errorMessage}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }
                    }
                    
                    if (uiState.searchHistory.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "搜索历史",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    
                                    TextButton(onClick = { viewModel.clearSearchHistory() }) {
                                        Text("清除")
                                    }
                                }
                                
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(uiState.searchHistory) { history ->
                                        HistoryItem(
                                            history = history,
                                            onClick = { 
                                                viewModel.onHistoryItemClick(history.keyword, history.platform)
                                            }
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

@Composable
fun HistoryItem(
    history: com.focus.app.domain.model.SearchHistory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = history.keyword,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "${getPlatformName(history.platform)} · ${formatTime(history.searchTime)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 1000 -> "刚刚"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}分钟前"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}小时前"
        else -> "${diff / (24 * 60 * 60 * 1000)}天前"
    }
}