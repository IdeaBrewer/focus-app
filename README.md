# Focus - 高效搜索与时间管理助手

<div align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions">
</div>

## 🎯 项目简介

Focus 是一款帮助用户快速搜索社交媒体内容并管理使用时间的Android应用。通过专注搜索入口和时间追踪功能，帮助用户避免沉迷，提高效率。

### 核心功能
- 🔍 **快速搜索**: 一键跳转到社交媒体搜索结果页面
- ⏱️ **时间追踪**: 自动记录各平台使用时长
- 🔔 **智能提醒**: 使用时间超时提醒
- 📊 **数据统计**: 详细的使用数据分析

## 🚀 快速开始

### 环境要求
- **Android Studio**: 最新版本
- **JDK**: 17+
- **Android SDK**: API 24+
- **Gradle**: 8.0+

### 克隆项目
```bash
git clone https://github.com/your-username/focus-app.git
cd focus-app
```

### 构建项目
```bash
# 授权gradlew
chmod +x gradlew

# 同步依赖
./gradlew build

# 运行测试
./gradlew test

# 构建APK
./gradlew assembleDebug
```

### 安装APK
```bash
# 安装到连接的设备
./gradlew installDebug
```

## 📱 功能特性

### 1. 专注搜索
- 支持小红书、抖音、B站等平台
- 直接跳转到搜索结果页面
- 避免首页内容干扰

### 2. 时间管理
- 自动检测APP使用状态
- 实时记录使用时长
- 精确到分钟的时间统计

### 3. 智能提醒
- 可自定义提醒间隔
- 悬浮窗形式显示
- 不影响正常使用

### 4. 数据分析
- 日/周/月使用统计
- 平台使用分布
- 使用趋势分析

## 🛠️ 技术架构

### 前端技术栈
- **UI框架**: Jetpack Compose
- **架构模式**: MVVM
- **依赖注入**: Hilt
- **数据存储**: Room + DataStore
- **异步处理**: Coroutines + Flow
- **导航**: Navigation Compose

### 后端服务
- **后台服务**: Android Service
- **任务调度**: WorkManager
- **权限管理**: Accessibility Service
- **悬浮窗**: System Overlay

### 开发工具
- **构建工具**: Gradle 8.0+
- **CI/CD**: GitHub Actions
- **代码质量**: ktlint + detekt
- **测试框架**: JUnit + Mockito

## 📁 项目结构

```
focus-app/
├── app/src/main/java/com/focus/app/
│   ├── ui/                    # UI层
│   │   ├── theme/            # 主题配置
│   │   ├── components/       # 可复用组件
│   │   ├── screens/          # 页面
│   │   └── navigation/       # 导航
│   ├── data/                 # 数据层
│   │   ├── local/           # 本地数据
│   │   └── repository/      # 数据仓库
│   ├── domain/              # 业务逻辑层
│   ├── service/             # 后台服务
│   └── utils/               # 工具类
├── .github/workflows/        # GitHub Actions
└── gradle/                   # Gradle配置
```

## 🔄 开发流程

### 1. 功能开发
1. 创建功能分支
2. 编写代码和测试
3. 提交Pull Request
4. 代码审查
5. 合并到主分支

### 2. 构建流程
- **开发构建**: 每次`push`到`develop`分支
- **测试构建**: 每次`push`到`main`分支
- **发布构建**: 创建GitHub Release时触发

### 3. APK获取
- **Debug APK**: GitHub Actions构建完成后下载
- **Release APK**: GitHub Releases页面下载

## 📋 GitHub Actions

### 工作流说明
- **build.yml**: 构建Debug APK
- **test.yml**: 运行测试和代码检查
- **release.yml**: 构建Release APK并发布

### 获取APK
1. 进入GitHub Actions页面
2. 选择对应的工作流
3. 查看构建历史
4. 下载构建产物

## 🔧 配置说明

### 签名配置（发布版本）
```bash
# 在GitHub Secrets中配置
KEYSTORE_BASE64=base64编码的keystore文件
KEYSTORE_PASSWORD=keystore密码
KEY_ALIAS=key别名
KEY_PASSWORD=key密码
```

### 应用配置
```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        applicationId = "com.focus.app"
        versionCode = 1
        versionName = "1.0.0"
    }
}
```

## 📝 贡献指南

### 开发规范
- 使用Kotlin编写代码
- 遵循MVVM架构模式
- 编写单元测试
- 使用ktlint格式化代码

### 提交规范
```bash
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式化
refactor: 重构
test: 测试相关
chore: 构建工具或依赖管理
```

### Pull Request流程
1. Fork项目
2. 创建功能分支
3. 提交代码
4. 创建PR
5. 等待审查
6. 合并代码

## 📄 许可证

本项目采用MIT许可证 - 详见[LICENSE](LICENSE)文件。

## 🤝 联系方式

- **项目地址**: [GitHub Repository](https://github.com/your-username/focus-app)
- **问题反馈**: [GitHub Issues](https://github.com/your-username/focus-app/issues)
- **邮箱**: your-email@example.com

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

---

<div align="center">
  <strong>Focus - 让搜索更专注，让时间更有价值</strong>
</div>