# Focus APP 界面原型设计

## 🎨 界面设计规范

### 颜色主题
```kotlin
// 主题颜色定义
val FocusColors = lightColorScheme(
    primary = Color(0xFFE60012),        // 小红书红
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE8E8),
    onPrimaryContainer = Color(0xFF410001),
    
    secondary = Color(0xFF77574A),       // 辅助色
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDDD1),
    onSecondaryContainer = Color(0xFF2C160C),
    
    background = Color(0xFFFFFBFF),      // 背景色
    onBackground = Color(0xFF1F1B16),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1F1B16),
    
    outline = Color(0xFF85736B),
    outlineVariant = Color(0xFFD7C2B8)
)
```

### 字体规范
```kotlin
val FocusTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    )
)
```

## 📱 详细界面设计

### 1. 主界面 (MainActivity.kt)

```kotlin
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 顶部标题
        Text(
            text = "Focus",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // 搜索区域
        SearchSection(
            onSearchClick = onNavigateToSearch,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // 平台选择区域
        PlatformSelector(
            selectedPlatform = uiState.selectedPlatform,
            onPlatformSelected = { viewModel.selectPlatform(it) },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // 使用统计区域
        UsageStatsSection(
            todayUsage = uiState.todayUsage,
            platformUsage = uiState.platformUsage,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // 底部导航
        BottomNavigation(
            onSettingsClick = onNavigateToSettings,
            onAboutClick = { /* 显示关于对话框 */ }
        )
    }
}

@Composable
fun SearchSection(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            placeholder = { Text("搜索关键词...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "搜索")
            }
        )
        
        Button(
            onClick = onSearchClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("搜索", color = Color.White)
        }
    }
}

@Composable
fun PlatformSelector(
    selectedPlatform: String,
    onPlatformSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "选择搜索平台",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PlatformButton(
                name = "小红书",
                icon = R.drawable.ic_xiaohongshu,
                isSelected = selectedPlatform == "xiaohongshu",
                onClick = { onPlatformSelected("xiaohongshu") }
            )
            
            PlatformButton(
                name = "抖音",
                icon = R.drawable.ic_douyin,
                isSelected = selectedPlatform == "douyin",
                onClick = { onPlatformSelected("douyin") }
            )
            
            PlatformButton(
                name = "B站",
                icon = R.drawable.ic_bilibili,
                isSelected = selectedPlatform == "bilibili",
                onClick = { onPlatformSelected("bilibili") }
            )
        }
    }
}

@Composable
fun PlatformButton(
    name: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier.size(64.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surface
            ),
            border = if (isSelected) 
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            else 
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = name,
                    tint = if (isSelected) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun UsageStatsSection(
    todayUsage: Long,
    platformUsage: Map<String, Long>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "今日使用时长",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = formatDuration(todayUsage),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Divider(modifier = Modifier.padding(bottom = 8.dp))
            
            platformUsage.forEach { (platform, duration) ->
                UsageItem(
                    platformName = getPlatformName(platform),
                    duration = duration,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun UsageItem(
    platformName: String,
    duration: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = platformName,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Text(
            text = formatDuration(duration),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
```

### 2. 搜索界面 (SearchActivity.kt)

```kotlin
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onBack: () -> Unit,
    onSearchSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 顶部导航
        TopAppBar(
            title = { Text("搜索") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                }
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // 搜索输入框
            OutlinedTextField(
                value = uiState.keyword,
                onValueChange = { viewModel.updateKeyword(it) },
                placeholder = { Text("请输入搜索关键词") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { viewModel.performSearch() }
                )
            )
            
            // 平台选择
            Text(
                text = "选择搜索平台",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.availablePlatforms) { platform ->
                    PlatformRadioItem(
                        platform = platform,
                        isSelected = uiState.selectedPlatform == platform.id,
                        onSelected = { viewModel.selectPlatform(platform.id) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 搜索按钮
            Button(
                onClick = { 
                    viewModel.performSearch()
                    onSearchSuccess()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.keyword.isNotBlank() && uiState.selectedPlatform != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState.isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("开始搜索", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun PlatformRadioItem(
    platform: Platform,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelected),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) 
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else 
            BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = platform.icon),
                contentDescription = platform.name,
                tint = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 16.dp)
            )
            
            Text(
                text = platform.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            
            RadioButton(
                selected = isSelected,
                onClick = onSelected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
```

### 3. 提醒对话框 (ReminderDialog.kt)

```kotlin
@Composable
fun ReminderDialog(
    onDismiss: () -> Unit,
    onContinue: () -> Unit,
    onStop: () -> Unit,
    onDismissForToday: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "使用提醒",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = "时间",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 16.dp)
                )
                
                Text(
                    text = "您已使用小红书30分钟",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "注意合理使用时间",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "今日总时长: 30分钟",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onStop,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("停止使用")
                }
                
                Button(
                    onClick = onContinue,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("继续使用", color = Color.White)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissForToday,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("今日不再提醒")
            }
        }
    )
}
```

## 🔄 状态管理架构

### ViewModel设计

```kotlin
class MainViewModel(
    private val usageRepository: UsageRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    init {
        // 监听使用时长变化
        viewModelScope.launch {
            usageRepository.todayUsage.collect { usage ->
                _uiState.update { it.copy(todayUsage = usage) }
            }
        }
        
        // 监听平台使用时长
        viewModelScope.launch {
            usageRepository.platformUsage.collect { usage ->
                _uiState.update { it.copy(platformUsage = usage) }
            }
        }
        
        // 加载设置
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            _uiState.update { it.copy(selectedPlatform = settings.defaultPlatform) }
        }
    }
    
    fun selectPlatform(platform: String) {
        _uiState.update { it.copy(selectedPlatform = platform) }
        viewModelScope.launch {
            settingsRepository.updateDefaultPlatform(platform)
        }
    }
}

data class MainUiState(
    val todayUsage: Long = 0L,
    val platformUsage: Map<String, Long> = emptyMap(),
    val selectedPlatform: String = "xiaohongshu",
    val isLoading: Boolean = false,
    val error: String? = null
)
```

## 🎯 交互设计细节

### 1. 手势支持
- **下拉刷新**: 主界面支持下拉刷新使用数据
- **长按操作**: 长按平台按钮可快速搜索
- **滑动返回**: 所有界面支持滑动返回

### 2. 动画效果
- **页面转场**: 使用共享元素动画
- **按钮反馈**: 点击时的涟漪效果
- **状态变化**: 平滑的过渡动画

### 3. 无障碍支持
- **屏幕阅读器**: 所有组件都有正确的contentDescription
- **大字体**: 支持系统字体大小设置
- **高对比度**: 支持系统高对比度模式

### 4. 响应式布局
```kotlin
@Composable
fun ResponsiveLayout(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    
    if (isPortrait) {
        // 竖屏布局
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    } else {
        // 横屏布局
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}
```

这个前端架构设计确保了：
- **界面简洁直观**: 核心功能一目了然
- **交互流畅**: 所有操作都有即时反馈
- **代码结构清晰**: MVVM架构，易于维护
- **扩展性好**: 组件化设计，便于添加新功能
- **用户体验佳**: 考虑了各种使用场景和边缘情况