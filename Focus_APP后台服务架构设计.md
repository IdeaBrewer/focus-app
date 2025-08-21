# Focus APP 后台服务架构设计

## 🎯 核心设计原则

### 1. 用户行为导向
- **专注搜索**: 用户搜索后直接进入目标APP，不会返回Focus
- **后台监控**: Focus需要在后台默默工作
- **非打扰**: 提醒以悬浮窗形式显示，不影响用户正常使用
- **透明性**: 用户可以随时查看使用数据

### 2. 技术架构要求
- **后台稳定性**: 确保服务不被系统杀死
- **低功耗**: 最小化电池消耗
- **实时性**: 准确的时间追踪和及时提醒
- **可靠性**: 数据不丢失，服务不崩溃

## 🏗️ 后台服务架构

### 核心服务组件
```
┌─────────────────────────────────────────────────────────────┐
│                    Focus 后台服务                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              FocusService                           │    │
│  │              (主服务 - 前台服务)                    │    │
│  └─────────────────────────────────────────────────────┘    │
│                    │                                       │
│        ┌───────────┼───────────┐                         │
│        │           │           │                         │
│  ┌─────▼─────┐ ┌───▼────┐ ┌───▼────┐                    │
│  │UsageTracker│ │Reminder│ │Overlay │                    │
│  │Service    │ │Service │ │Service │                    │
│  └───────────┘ └────────┘ └────────┘                    │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐    │
│  │                  数据同步层                           │    │
│  └─────────────────────────────────────────────────────┘    │
│                    │                                       │
│        ┌───────────┼───────────┐                         │
│        │           │           │                         │
│  ┌─────▼─────┐ ┌───▼────┐ ┌───▼────┐                    │
│  │   Room    │ │Shared  │ │Work    │                    │
│  │ Database  │ │Prefs   │ │Manager │                    │
│  └───────────┘ └────────┘ └────────┘                    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## 🔧 核心服务实现

### 1. FocusService (主服务)
```kotlin
@AndroidEntryPoint
class FocusService : Service() {
    
    private val binder = LocalBinder()
    private val usageTracker by lazy { UsageTracker(this) }
    private val reminderManager by lazy { ReminderManager(this) }
    private val overlayManager by lazy { OverlayManager(this) }
    
    inner class LocalBinder : Binder() {
        fun getService(): FocusService = this@FocusService
    }
    
    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        initializeServices()
    }
    
    override fun onBind(intent: Intent): IBinder = binder
    
    private fun startForegroundService() {
        val notification = createForegroundNotification()
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun createForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Focus正在运行")
            .setContentText("正在监控您的使用时间")
            .setSmallIcon(R.drawable.ic_focus)
            .setOngoing(true)
            .build()
    }
    
    private fun initializeServices() {
        usageTracker.startTracking()
        reminderManager.startMonitoring()
        overlayManager.initialize()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        usageTracker.stopTracking()
        reminderManager.stopMonitoring()
        overlayManager.cleanup()
    }
}
```

### 2. UsageTracker (使用追踪服务)
```kotlin
class UsageTracker(private val context: Context) {
    
    private val accessibilityManager by lazy {
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    }
    
    private val usageStatsManager by lazy {
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }
    
    private val database by lazy { AppDatabase.getInstance(context) }
    
    private var isTracking = false
    private var currentSession: UsageSession? = null
    private var trackingJob: Job? = null
    
    fun startTracking() {
        if (isTracking) return
        
        isTracking = true
        trackingJob = CoroutineScope(Dispatchers.IO).launch {
            while (isTracking) {
                trackAppUsage()
                delay(1000) // 每秒检查一次
            }
        }
    }
    
    fun stopTracking() {
        isTracking = false
        trackingJob?.cancel()
        currentSession?.let { session ->
            saveSession(session)
            currentSession = null
        }
    }
    
    private suspend fun trackAppUsage() {
        val foregroundApp = getForegroundApp()
        val supportedApps = getSupportedApps()
        
        if (foregroundApp in supportedApps) {
            if (currentSession == null) {
                // 开始新的使用会话
                currentSession = UsageSession(
                    appName = foregroundApp,
                    startTime = System.currentTimeMillis(),
                    endTime = System.currentTimeMillis()
                )
            } else {
                // 更新当前会话
                currentSession?.endTime = System.currentTimeMillis()
            }
        } else {
            // 结束当前会话
            currentSession?.let { session ->
                saveSession(session)
                currentSession = null
            }
        }
    }
    
    private fun getForegroundApp(): String? {
        // 使用UsageStatsManager获取前台应用
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 * 60 // 1分钟内
        
        val usageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )
        
        return usageStats
            .filter { it.lastTimeUsed == endTime }
            .maxByOrNull { it.lastTimeUsed }
            ?.packageName
    }
    
    private suspend fun saveSession(session: UsageSession) {
        database.usageDao().insertUsage(
            UsageEntity(
                appName = session.appName,
                startTime = session.startTime,
                endTime = session.endTime,
                duration = session.endTime - session.startTime
            )
        )
    }
    
    fun getTodayUsage(): Map<String, Long> {
        val todayStart = LocalDate.now().atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        val todayEnd = System.currentTimeMillis()
        
        return database.usageDao().getUsageBetween(todayStart, todayEnd)
            .groupBy { it.appName }
            .mapValues { (_, entities) ->
                entities.sumOf { it.duration }
            }
    }
}

data class UsageSession(
    val appName: String,
    val startTime: Long,
    var endTime: Long
)
```

### 3. ReminderManager (提醒管理服务)
```kotlin
class ReminderManager(private val context: Context) {
    
    private val usageTracker by lazy { UsageTracker(context) }
    private val overlayManager by lazy { OverlayManager(context) }
    private val settings by lazy { PreferenceManager.getDefaultSharedPreferences(context) }
    
    private var isMonitoring = false
    private var monitoringJob: Job? = null
    private var lastReminderTime = 0L
    
    fun startMonitoring() {
        if (isMonitoring) return
        
        isMonitoring = true
        monitoringJob = CoroutineScope(Dispatchers.Main).launch {
            while (isMonitoring) {
                checkAndTriggerReminder()
                delay(30000) // 每30秒检查一次
            }
        }
    }
    
    fun stopMonitoring() {
        isMonitoring = false
        monitoringJob?.cancel()
    }
    
    private suspend fun checkAndTriggerReminder() {
        val reminderInterval = settings.getLong("reminder_interval", 30 * 60 * 1000L) // 30分钟
        val currentTime = System.currentTimeMillis()
        
        // 检查是否在"今日不再提醒"期间
        val dismissedUntil = settings.getLong("dismissed_until", 0L)
        if (currentTime < dismissedUntil) {
            return
        }
        
        val todayUsage = usageTracker.getTodayUsage()
        val totalUsage = todayUsage.values.sum()
        
        if (totalUsage >= reminderInterval && currentTime - lastReminderTime > reminderInterval) {
            triggerReminder(totalUsage)
            lastReminderTime = currentTime
        }
    }
    
    private fun triggerReminder(totalUsage: Long) {
        overlayManager.showReminderOverlay(totalUsage)
    }
    
    fun onUserContinue() {
        // 用户选择继续使用，重置计时器
        lastReminderTime = System.currentTimeMillis()
    }
    
    fun onUserStop() {
        // 用户选择停止使用，记录这个决定
        settings.edit().putLong("stop_until", System.currentTimeMillis() + 60 * 60 * 1000L).apply()
    }
    
    fun onDismissForToday() {
        // 用户选择今日不再提醒
        val tomorrow = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        settings.edit().putLong("dismissed_until", tomorrow).apply()
    }
}
```

### 4. OverlayManager (悬浮窗管理服务)
```kotlin
class OverlayManager(private val context: Context) {
    
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var isOverlayShown = false
    
    fun initialize() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    
    fun showReminderOverlay(totalUsage: Long) {
        if (isOverlayShown || !hasOverlayPermission()) return
        
        val layoutParams = createOverlayLayoutParams()
        overlayView = createReminderView(totalUsage)
        
        windowManager?.addView(overlayView, layoutParams)
        isOverlayShown = true
    }
    
    fun hideReminderOverlay() {
        if (!isOverlayShown) return
        
        overlayView?.let { view ->
            windowManager?.removeView(view)
            overlayView = null
        }
        isOverlayShown = false
    }
    
    private fun createOverlayLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = 100
        }
    }
    
    private fun createReminderView(totalUsage: Long): View {
        return LayoutInflater.from(context).inflate(R.layout.overlay_reminder, null).apply {
            // 设置内容
            findViewById<TextView>(R.id.tvUsageTime).text = formatDuration(totalUsage)
            
            // 设置按钮点击事件
            findViewById<Button>(R.id.btnContinue).setOnClickListener {
                hideReminderOverlay()
                // 通知ReminderManager用户选择继续
            }
            
            findViewById<Button>(R.id.btnStop).setOnClickListener {
                hideReminderOverlay()
                // 通知ReminderManager用户选择停止
            }
            
            findViewById<Button>(R.id.btnDismiss).setOnClickListener {
                hideReminderOverlay()
                // 通知ReminderManager用户选择今日不再提醒
            }
            
            // 添加拖拽功能
            setOnTouchListener(OverlayTouchListener(this))
        }
    }
    
    private fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }
    
    fun cleanup() {
        hideReminderOverlay()
    }
}

class OverlayTouchListener(private val view: View) : View.OnTouchListener {
    
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = view.layoutParams.x
                initialY = view.layoutParams.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - initialTouchX
                val dy = event.rawY - initialTouchY
                
                view.layoutParams.x = initialX + dx.toInt()
                view.layoutParams.y = initialY + dy.toInt()
                
                view.windowManager?.updateViewLayout(view, view.layoutParams)
                return true
            }
        }
        return false
    }
}
```

## 🔄 服务生命周期管理

### 1. 服务启动时机
```kotlin
class FocusApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 检查是否需要启动后台服务
        if (shouldStartBackgroundService()) {
            startBackgroundService()
        }
    }
    
    private fun shouldStartBackgroundService(): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getBoolean("service_enabled", true)
    }
    
    private fun startBackgroundService() {
        val serviceIntent = Intent(this, FocusService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
}
```

### 2. 服务保活机制
```kotlin
class FocusService : Service() {
    
    private val wakeLock: PowerManager.WakeLock by lazy {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Focus::ServiceWakeLock")
    }
    
    override fun onCreate() {
        super.onCreate()
        acquireWakeLock()
        startPeriodicTasks()
    }
    
    private fun acquireWakeLock() {
        if (!wakeLock.isHeld) {
            wakeLock.acquire()
        }
    }
    
    private fun startPeriodicTasks() {
        // 定期检查服务状态
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(60000) // 每分钟检查一次
                ensureServiceRunning()
            }
        }
    }
    
    private fun ensureServiceRunning() {
        if (!isServiceRunning()) {
            restartService()
        }
    }
    
    private fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = manager.getRunningServices(Integer.MAX_VALUE)
        return services.any { it.service.className == FocusService::class.java.name }
    }
    
    private fun restartService() {
        val serviceIntent = Intent(this, FocusService::class.java)
        startService(serviceIntent)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }
}
```

## 📊 数据同步机制

### 1. 实时数据同步
```kotlin
class DataSyncManager(private val context: Context) {
    
    private val database by lazy { AppDatabase.getInstance(context) }
    private val usageTracker by lazy { UsageTracker(context) }
    
    suspend fun syncUsageData() {
        val latestUsage = usageTracker.getTodayUsage()
        
        // 更新数据库
        latestUsage.forEach { (appName, duration) ->
            database.usageDao().updateTodayUsage(appName, duration)
        }
        
        // 通知UI更新
        notifyUIUpdate()
    }
    
    private fun notifyUIUpdate() {
        // 发送广播通知UI更新
        val intent = Intent(ACTION_USAGE_UPDATED)
        context.sendBroadcast(intent)
    }
}
```

### 2. UI数据观察
```kotlin
@Composable
fun observeUsageData(): StateFlow<Map<String, Long>> {
    val context = LocalContext.current
    val usageData = remember { mutableStateOf<Map<String, Long>>(emptyMap()) }
    
    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == ACTION_USAGE_UPDATED) {
                    // 重新获取数据
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = UsageTracker(context!!).getTodayUsage()
                        usageData.value = data
                    }
                }
            }
        }
        
        val filter = IntentFilter(ACTION_USAGE_UPDATED)
        context.registerReceiver(receiver, filter)
        
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
    
    return usageData
}
```

## 🔐 权限管理

### 1. 权限检查和申请
```kotlin
class PermissionManager(private val context: Context) {
    
    fun checkAndRequestPermissions(): Boolean {
        return checkAccessibilityPermission() && 
               checkUsageStatsPermission() && 
               checkOverlayPermission()
    }
    
    private fun checkAccessibilityPermission(): Boolean {
        val serviceId = "${context.packageName}/.FocusService"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return enabledServices?.contains(serviceId) == true
    }
    
    private fun checkUsageStatsPermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    
    private fun checkOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(context)
    }
    
    fun requestPermissions() {
        // 引导用户到系统设置页面
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }
}
```

## 📈 性能优化

### 1. 电池优化
```kotlin
class BatteryOptimizer {
    
    fun optimizeBatteryUsage(context: Context) {
        // 请求忽略电池优化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:${context.packageName}")
            context.startActivity(intent)
        }
    }
    
    fun scheduleWork(context: Context) {
        // 使用WorkManager进行定期任务
        val workRequest = PeriodicWorkRequestBuilder<UsageSyncWorker>(
            15, // 15分钟间隔
            TimeUnit.MINUTES
        ).build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "usage_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

class UsageSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val syncManager = DataSyncManager(applicationContext)
            syncManager.syncUsageData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
```

这个后台服务架构确保了：
- **用户搜索后不返回**: Focus在后台默默工作
- **实时监控**: 准确追踪使用时长
- **及时提醒**: 悬浮窗形式不打扰用户
- **数据同步**: UI和服务数据实时同步
- **服务稳定**: 多重保活机制确保服务不崩溃