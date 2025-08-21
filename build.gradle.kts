# Focus APP 项目初始化脚本

## 🚀 快速开始

### 1. 创建GitHub仓库
```bash
# 1. 在GitHub上创建新仓库
# 仓库名: focus-app
# 描述: Focus - 高效搜索与时间管理助手
# 设为Public（免费账号只能用Public仓库的GitHub Actions）

# 2. 克隆仓库到本地
git clone https://github.com/your-username/focus-app.git
cd focus-app
```

### 2. 初始化Android项目
```bash
# 创建Android项目基础结构
mkdir -p app/src/main/java/com/focus/app/{ui,data,domain,service,utils,receiver}
mkdir -p app/src/main/res/{drawable,mipmap,values,xml}
mkdir -p app/src/{debug,androidTest}/java/com/focus/app
mkdir -p .github/workflows

# 创建必要的文件
touch README.md
touch LICENSE
touch .gitignore
touch gradle.properties
touch settings.gradle.kts
touch build.gradle.kts
touch app/build.gradle.kts
touch app/proguard-rules.pro
```

### 3. 配置Gradle文件
Write
<arg_key>content</arg_key>
<arg_value># 根目录的 build.gradle.kts
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