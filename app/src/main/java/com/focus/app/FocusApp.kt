package com.focus.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FocusApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize app components
        initializeApp()
    }
    
    private fun initializeApp() {
        // TODO: Initialize crash reporting, analytics, etc.
    }
}