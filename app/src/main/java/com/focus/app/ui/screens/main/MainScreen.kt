package com.focus.app.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.focus.app.ui.components.SearchBar
import com.focus.app.ui.components.PlatformSelector
import com.focus.app.ui.components.UsageStatsCard

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // App Header
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Focus",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "专注搜索，高效管理",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Search Section
        SearchBar(
            onSearchClick = onNavigateToSearch,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Platform Selection
        PlatformSelector(
            selectedPlatform = uiState.selectedPlatform,
            onPlatformSelected = { viewModel.selectPlatform(it) },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Usage Statistics
        UsageStatsCard(
            todayUsage = uiState.todayUsage,
            platformUsage = uiState.platformUsage,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.weight(1f)
            ) {
                Text("设置")
            }
            
            Button(
                onClick = { /* TODO: Show about dialog */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("关于")
            }
        }
    }
}