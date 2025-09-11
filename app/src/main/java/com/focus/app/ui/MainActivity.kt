package com.focus.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Simple TextView to show app is working
        val titleText = findViewById<TextView>(R.id.main_title)
        titleText.text = "Focus App"
    }
}