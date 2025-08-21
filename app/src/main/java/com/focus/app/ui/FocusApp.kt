package com.focus.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.focus.app.ui.navigation.FocusRoutes
import com.focus.app.ui.screens.main.MainScreen
import com.focus.app.ui.screens.main.MainViewModel
import com.focus.app.ui.screens.search.SearchScreen
import com.focus.app.ui.screens.search.SearchViewModel
import com.focus.app.ui.screens.settings.SettingsScreen
import com.focus.app.ui.screens.settings.SettingsViewModel

@Composable
fun FocusApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = FocusRoutes.Main.route,
        modifier = modifier
    ) {
        composable(FocusRoutes.Main.route) {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            MainScreen(
                uiState = uiState,
                onNavigateToSearch = {
                    navController.navigate(FocusRoutes.Search.route)
                },
                onNavigateToSettings = {
                    navController.navigate(FocusRoutes.Settings.route)
                },
                onEvent = { event ->
                    viewModel.onEvent(event)
                }
            )
        }
        
        composable(FocusRoutes.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            SearchScreen(
                uiState = uiState,
                onBack = {
                    navController.popBackStack()
                },
                onEvent = { event ->
                    viewModel.onEvent(event)
                }
            )
        }
        
        composable(FocusRoutes.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            SettingsScreen(
                uiState = uiState,
                onBack = {
                    navController.popBackStack()
                },
                onEvent = { event ->
                    viewModel.onEvent(event)
                }
            )
        }
    }
}