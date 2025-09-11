package com.focus.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.focus.app.ui.navigation.FocusRoute
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
        startDestination = FocusRoute.Main.route,
        modifier = modifier
    ) {
        composable(FocusRoute.Main.route) {
            MainScreen(
                onNavigateToSearch = {
                    navController.navigate(FocusRoute.Search.route)
                },
                onNavigateToSettings = {
                    navController.navigate(FocusRoute.Settings.route)
                }
            )
        }
        
        composable(FocusRoute.Search.route) {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(FocusRoute.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}