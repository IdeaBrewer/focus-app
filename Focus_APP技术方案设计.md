# Focus APP 技术方案设计

## 🛠️ 技术栈选型

### 前端技术栈
| 技术类别 | 技术选择 | 版本 | 说明 |
|---------|----------|------|------|
| **开发语言** | Kotlin | 1.9.0 | 现代化Android开发语言 |
| **UI框架** | Jetpack Compose | 1.5.0 | 声明式UI框架 |
| **架构组件** | AndroidX Architecture Components | 最新 | MVVM架构支持 |
| **依赖注入** | Hilt | 2.47 | 现代化DI框架 |
| **数据库** | Room | 2.6.0 | 本地数据存储 |
| **网络请求** | Retrofit + OkHttp | 2.9.0 | 网络请求框架 |
| **异步处理** | Coroutines + Flow | 1.7.0 | 异步编程 |
| **生命周期** | Lifecycle | 2.6.0 | 生命周期管理 |
| **导航** | Navigation Compose | 2.7.0 | 页面导航 |

### 后端技术栈
| 技术类别 | 技术选择 | 版本 | 说明 |
|---------|----------|------|------|
| **后台服务** | Android Service | - | 系统后台服务 |
| **数据存储** | Room Database | 2.6.0 | 本地SQLite |
| **偏好设置** | DataStore | 1.0.0 | 替代SharedPreferences |
| **工作调度** | WorkManager | 2.8.0 | 后台任务调度 |
| **权限管理** | Android Permissions | - | 系统权限管理 |
| **无障碍** | Accessibility Service | - | APP状态监听 |
| **悬浮窗** | System Overlay | - | 跨APP显示 |

### 开发工具
| 工具类型 | 工具选择 | 说明 |
|---------|----------|------|
| **IDE** | Android Studio | 官方开发环境 |
| **构建工具** | Gradle | 8.0 | 项目构建 |
| **版本控制** | Git | - | 代码管理 |
| **CI/CD** | GitHub Actions | 自动化构建和测试 |
| **代码质量** | ktlint | 代码风格检查 |
| **单元测试** | JUnit + Mockito | 单元测试框架 |

## 📁 项目文件结构

```
focus-app/
├── .github/                           # GitHub Actions配置
│   └── workflows/
│       ├── build.yml                  # 构建APK
│       ├── test.yml                   # 运行测试
│       └── release.yml                # 发布版本
├── app/                               # 主应用模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/focus/app/
│   │   │   │   ├── FocusApp.kt        # Application类
│   │   │   │   ├── di/                # 依赖注入
│   │   │   │   │   ├── AppModule.kt
│   │   │   │   │   └── ServiceModule.kt
│   │   │   │   ├── ui/                # UI层
│   │   │   │   │   ├── theme/         # 主题
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   ├── components/    # 可复用组件
│   │   │   │   │   │   ├── SearchBar.kt
│   │   │   │   │   │   ├── PlatformSelector.kt
│   │   │   │   │   │   └── UsageCard.kt
│   │   │   │   │   ├── screens/       # 页面
│   │   │   │   │   │   ├── main/
│   │   │   │   │   │   │   ├── MainScreen.kt
│   │   │   │   │   │   │   └── MainViewModel.kt
│   │   │   │   │   │   ├── search/
│   │   │   │   │   │   │   ├── SearchScreen.kt
│   │   │   │   │   │   │   └── SearchViewModel.kt
│   │   │   │   │   │   └── settings/
│   │   │   │   │   │       ├── SettingsScreen.kt
│   │   │   │   │   │       └── SettingsViewModel.kt
│   │   │   │   │   └── navigation/     # 导航
│   │   │   │   │       ├── FocusNavHost.kt
│   │   │   │   │       └── FocusRoutes.kt
│   │   │   │   ├── data/               # 数据层
│   │   │   │   │   ├── local/          # 本地数据
│   │   │   │   │   │   ├── database/   # 数据库
│   │   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   │   ├── entity/
│   │   │   │   │   │   │   │   ├── UsageEntity.kt
│   │   │   │   │   │   │   │   └── SettingsEntity.kt
│   │   │   │   │   │   │   └── dao/
│   │   │   │   │   │   │       ├── UsageDao.kt
│   │   │   │   │   │   │       └── SettingsDao.kt
│   │   │   │   │   │   └── preference/ # 偏好设置
│   │   │   │   │   │       └── FocusPreferences.kt
│   │   │   │   │   └── repository/     # 数据仓库
│   │   │   │   │       ├── UsageRepository.kt
│   │   │   │   │       ├── SettingsRepository.kt
│   │   │   │   │       └── SearchRepository.kt
│   │   │   │   ├── domain/             # 业务逻辑层
│   │   │   │   │   ├── model/          # 数据模型
│   │   │   │   │   │   ├── UsageModel.kt
│   │   │   │   │   │   ├── PlatformModel.kt
│   │   │   │   │   │   └── SettingsModel.kt
│   │   │   │   │   ├── usecase/        # 用例
│   │   │   │   │   │   ├── GetUsageUseCase.kt
│   │   │   │   │   │   ├── SaveUsageUseCase.kt
│   │   │   │   │   │   └── UpdateSettingsUseCase.kt
│   │   │   │   │   └── repository/     # 仓库接口
│   │   │   │   │       ├── IUsageRepository.kt
│   │   │   │   │       └── ISettingsRepository.kt
│   │   │   │   ├── service/            # 后台服务
│   │   │   │   │   ├── FocusService.kt
│   │   │   │   │   ├── UsageTrackerService.kt
│   │   │   │   │   ├── ReminderService.kt
│   │   │   │   │   └── OverlayService.kt
│   │   │   │   ├── utils/              # 工具类
│   │   │   │   │   ├── Extensions.kt
│   │   │   │   │   ├── PermissionUtils.kt
│   │   │   │   │   └── DateUtils.kt
│   │   │   │   └── receiver/           # 广播接收器
│   │   │   │       ├── BootReceiver.kt
│   │   │   │       └── UsageUpdateReceiver.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/           # 图片资源
│   │   │   │   ├── mipmap/             # 应用图标
│   │   │   │   ├── values/             # 值资源
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── dimens.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── xml/                # XML配置
│   │   │   │       ├── network_security_config.xml
│   │   │   │       └── provider_paths.xml
│   │   │   └── AndroidManifest.xml    # 应用清单
│   │   └── debug/                      # 调试版本
│   │   └── main/                       # 测试版本
│   ├── build.gradle.kts                # 应用级构建配置
│   └── proguard-rules.pro              # 混淆规则
├── build.gradle.kts                    # 项目级构建配置
├── gradle.properties                  # Gradle属性
├── settings.gradle.kts                 # Gradle设置
├── gradlew                             # Gradle包装器
├── gradlew.bat                         # Windows Gradle包装器
├── README.md                           # 项目说明
├── LICENSE                             # 许可证
├── .gitignore                          # Git忽略文件
├── ktlint.yml                          # 代码风格配置
├── CODE_OF_CONDUCT.md                  # 行为准则
└── CONTRIBUTING.md                      # 贡献指南
```

## 🚀 GitHub Actions 配置

### 1. 构建APK工作流

```yaml
# .github/workflows/build.yml
name: Build APK

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
          
    - name: Build debug APK
      run: ./gradlew assembleDebug
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk
        
    - name: Upload build outputs
      uses: actions/upload-artifact@v3
      with:
        name: build-outputs
        path: app/build/outputs/
```

### 2. 测试工作流

```yaml
# .github/workflows/test.yml
name: Run Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
          
    - name: Run unit tests
      run: ./gradlew test
      
    - name: Run lint checks
      run: ./gradlew lint
      
    - name: Upload test results
      uses: actions/upload-artifact@v3
      if: always()
      with:
        name: test-results
        path: app/build/test-results/
        
    - name: Upload lint results
      uses: actions/upload-artifact@v3
      if: always()
      with:
        name: lint-results
        path: app/build/reports/lint-results.html
```

### 3. 发布版本工作流

```yaml
# .github/workflows/release.yml
name: Release APK

on:
  release:
    types: [published]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Cache Gradle packages
      uses: actions/cache@v3
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
          
    - name: Build release APK
      run: ./gradlew assembleRelease
      env:
        KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
        KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
        KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
        
    - name: Sign APK
      run: |
        echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 -d > release.keystore
        ./gradlew assembleRelease
        jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
          -keystore release.keystore \
          -storepass ${{ secrets.KEYSTORE_PASSWORD }} \
          -keypass ${{ secrets.KEY_PASSWORD }} \
          app/build/outputs/apk/release/app-release-unsigned.apk \
          ${{ secrets.KEY_ALIAS }}
          
    - name: Upload release APK
      uses: actions/upload-artifact@v3
      with:
        name: release-apk
        path: app/build/outputs/apk/release/app-release.apk
        
    - name: Upload to GitHub Release
      uses: actions/upload-release-asset@v1
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
      with:
        upload_url: ${{ github.event.release.upload_url }}
        asset_path: app/build/outputs/apk/release/app-release.apk
        asset_name: Focus-${{ github.event.release.tag_name }}.apk
        asset_content_type: application/vnd.android.package-archive
```

## 📋 核心配置文件

### 1. 项目级 build.gradle.kts

```kotlin
// build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
```

### 2. 应用级 build.gradle.kts

```kotlin
// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.focus.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.focus.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.navigation)
    
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    
    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    
    // DataStore
    implementation(libs.datastore.preferences)
    
    // WorkManager
    implementation(libs.work.runtime)
    implementation(libs.work.runtime.ktx)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}
```

### 3. 版本目录 (gradle/libs.versions.toml)

```toml
[versions]
agp = "8.0.0"
kotlin = "1.9.0"
compose = "1.5.0"
hilt = "2.47"
room = "2.6.0"
coroutines = "1.7.0"
datastore = "1.0.0"
work = "2.8.0"
junit = "4.13.2"
mockito = "5.0.0"
androidx-test = "1.5.0"
espresso = "3.5.0"

[libraries]
androidx-core-ktx = "androidx.core:core-ktx:1.12.0"
androidx-lifecycle-runtime-ktx = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.0"
androidx-activity-compose = "androidx.activity:activity-compose:1.8.0"

compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }
compose-navigation = "androidx.navigation:navigation-compose:2.7.0"

hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
androidx-hilt-navigation-compose = "androidx.hilt:hilt-navigation-compose:1.0.0"

room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "coroutines" }
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines" }

datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }

work-runtime = { group = "androidx.work", name = "work-runtime", version.ref = "work" }
work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }

junit = { group = "junit", name = "junit", version.ref = "junit" }
mockito-core = { group = "org.mockito", name = "mockito-core", version.ref = "mockito" }
mockito-kotlin = { group = "org.mockito.kotlin", name = "mockito-kotlin", version.ref = "mockito" }

androidx-junit = "androidx.test.ext:junit:1.1.5"
androidx-espresso-core = "androidx.test.espresso:espresso-core:3.5.0"
compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
kotlin-kapt = { id = "org.jetbrains.kotlin.kapt", version.ref = "kotlin" }
kotlin-parcelize = { id = "org.jetbrains.kotlin.plugin.parcelize", version.ref = "kotlin" }
```

## 📋 开发指南

### 1. 环境要求
- **Android Studio**: 最新版本
- **JDK**: 17+
- **Android SDK**: API 24+
- **Gradle**: 8.0+

### 2. 开发流程
1. **克隆项目**: `git clone [repository-url]`
2. **打开项目**: 在Android Studio中打开
3. **同步项目**: 等待Gradle同步完成
4. **构建项目**: 运行 `./gradlew build`
5. **运行测试**: 运行 `./gradlew test`
6. **安装APK**: 运行 `./gradlew installDebug`

### 3. 代码规范
- 使用 **Kotlin** 编写代码
- 遵循 **MVVM** 架构模式
- 使用 **Compose** 构建UI
- 使用 **Hilt** 进行依赖注入
- 使用 **Room** 进行数据存储
- 使用 **Coroutines** 进行异步处理

### 4. 测试策略
- **单元测试**: 测试业务逻辑
- **集成测试**: 测试组件交互
- **UI测试**: 测试用户界面
- **端到端测试**: 测试完整流程

### 5. 发布流程
1. **代码审查**: 通过PR进行代码审查
2. **自动化测试**: GitHub Actions运行测试
3. **构建APK**: 自动构建debug和release版本
4. **发布版本**: 创建GitHub Release
5. **分发APK**: 通过GitHub Releases分发

这个技术方案提供了完整的开发环境配置，确保了项目的可维护性和可扩展性。