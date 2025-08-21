package com.focus.app.data.service

import android.content.Context
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import com.focus.app.MainActivity
import com.focus.app.R

class ReminderManagerService(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "focus_reminder_channel"

    init {
        createNotificationChannel()
    }

    fun showReminderNotification(appName: String, usageTime: Long) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(context, channelId)
            .setContentTitle("使用时长提醒")
            .setContentText("您在 $appName 上已使用 ${formatDuration(usageTime)}，注意适当休息")
            .setSmallIcon(R.drawable.ic_notification) // 需要创建这个图标
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    fun showOverlayReminder(appName: String, usageTime: Long) {
        // This will be implemented with Overlay Service
        // For now, we'll just show a notification
        showReminderNotification(appName, usageTime)
    }

    fun dismissReminder() {
        notificationManager.cancel(1)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Focus提醒",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "应用使用时长提醒"
                enableLights(true)
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(channel)
        }
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
}