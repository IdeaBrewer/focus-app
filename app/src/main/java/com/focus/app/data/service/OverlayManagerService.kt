package com.focus.app.data.service

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.focus.app.R

class OverlayManagerService(private val context: Context) {

    private var windowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var overlayView: View? = null

    fun showOverlay(appName: String, usageTime: Long) {
        if (overlayView != null) {
            hideOverlay()
        }

        val inflater = LayoutInflater.from(context)
        overlayView = inflater.inflate(R.layout.overlay_reminder, null)

        val appNameText = overlayView?.findViewById<TextView>(R.id.app_name_text)
        val usageTimeText = overlayView?.findViewById<TextView>(R.id.usage_time_text)
        val dismissButton = overlayView?.findViewById<TextView>(R.id.dismiss_button)

        appNameText?.text = appName
        usageTimeText?.text = formatDuration(usageTime)

        dismissButton?.setOnClickListener {
            hideOverlay()
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 100
        }

        try {
            windowManager.addView(overlayView, params)
        } catch (e: Exception) {
            // Handle overlay permission denied
        }
    }

    fun hideOverlay() {
        overlayView?.let { view ->
            try {
                windowManager.removeView(view)
                overlayView = null
            } catch (e: Exception) {
                // View not attached to window manager
            }
        }
    }

    fun isOverlayShowing(): Boolean {
        return overlayView != null
    }

    private fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        
        return when {
            hours > 0 -> "${hours}小时${minutes}分钟"
            minutes > 0 -> "${minutes}分钟"
            else -> "刚刚"
        }
    }

    fun hasOverlayPermission(): Boolean {
        return android.provider.Settings.canDrawOverlays(context)
    }
}