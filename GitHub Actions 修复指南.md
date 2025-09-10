# GitHub Actions 修复方案

## 快速修复步骤

请按照以下步骤修复 GitHub Actions：

### 方法1：直接在 GitHub 网页上修复

1. 访问你的仓库：https://github.com/IdeaBrewer/focus-app
2. 点击 **Add file** → **Create new file**
3. 文件路径：`.github/workflows/build.yml`
4. 复制以下内容到文件中：

```yaml
name: Build Debug APK

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4
      
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Setup Android SDK
      uses: android-actions/setup-android@v3
      
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Cache Gradle packages
      uses: actions/cache@v4
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
          
    - name: Build debug APK
      run: ./gradlew assembleDebug
      
    - name: Upload debug APK
      uses: actions/upload-artifact@v4
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk
        retention-days: 30
```

5. 点击 **Commit new file**

### 方法2：删除失败的工作流重新创建

如果还是有问题，可以：

1. 进入 **Actions** 页面
2. 点击左侧的 **Build APK**
3. 点击 **Delete workflow** 删除失败的工作流
4. 按照方法1重新创建

### 方法3：使用 Git Bash 运行修复脚本

如果你有 Git Bash 环境，可以运行：

```bash
cd /d/aa_code/focus
chmod +x fix-github-actions.sh
./fix-github-actions.sh
```

## 主要修复点

1. **修复了 YAML 格式错误**：原来的配置文件有格式问题
2. **简化了配置**：移除了可能导致问题的复杂步骤
3. **使用了稳定版本的 Actions**：`v4` 和 `v3`
4. **添加了缓存**：提升编译速度
5. **明确的分支配置**：支持 `main` 和 `master` 分支

## 验证编译

修复后，你应该能看到：

1. **Actions 页面**显示绿色对勾
2. **编译时间**约5-10分钟
3. **Artifacts** 中有 `debug-apk` 文件
4. **文件大小**约 10-20MB

如果还有问题，请告诉我具体的错误信息，我会继续帮你解决。