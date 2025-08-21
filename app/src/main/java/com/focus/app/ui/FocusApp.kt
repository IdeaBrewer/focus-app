package com.focus.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.focus.app.ui.navigation.FocusRoutes
import com.focus.app.ui.screens.MainScreen
import com.focus.app.ui.screens.SearchScreen
import com.focus.app.ui.screens.SettingsScreen

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
            MainScreen(
                onNavigateToSearch = {
                    navController.navigate(FocusRoutes.Search.route)
                },
                onNavigateToSettings = {
                    navController.navigate(FocusRoutes.Settings.route)
                }
            )
        }
        
        composable(FocusRoutes.Search.route) {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(FocusRoutes.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}