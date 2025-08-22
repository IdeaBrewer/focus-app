# GitHub Actions 自动编译 APK 设置指南

## 快速设置步骤

### 1. 创建 GitHub Actions 工作流

在你的 GitHub 仓库中：

1. 进入你的仓库：`https://github.com/IdeaBrewer/focus-app`
2. 点击 **Actions** 标签
3. 点击 **New workflow**
4. 选择 **Simple workflow**
5. 删除默认内容，复制下面的配置：

```yaml
name: Build APK

on:
  push:
    branches: [ master, main ]
  pull_request:
    branches: [ master, main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
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
          
    - name: Build Debug APK
      run: ./gradlew assembleDebug
      
    - name: Upload Debug APK
      uses: actions/upload-artifact@v4
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk
        retention-days: 30
```

6. 点击 **Start commit**，然后 **Commit new file**

### 2. 手动触发编译

1. 提交代码后，GitHub Actions 会自动开始编译
2. 你也可以手动触发：进入 **Actions** 页面，点击左侧的 **Build APK**，然后点击 **Run workflow**

### 3. 下载 APK

编译完成后：

1. 进入 **Actions** 页面
2. 点击最新的编译记录
3. 在左侧菜单中点击 **Artifacts**
4. 点击 **debug-apk** 下载 ZIP 文件
5. 解压后得到 `app-debug.apk` 文件

### 4. 安装测试

将 APK 文件传输到你的 Android 手机：

- 通过 USB 数据线
- 通过聊天工具发送
- 通过邮件发送

然后在手机上安装并测试。

## 优化设置（可选）

### 启用缓存加速编译

在 `Cache Gradle packages` 步骤中已经配置了缓存，可以大幅提升编译速度。

### 设置 Release APK 编译

如果要编译签名版的 Release APK，需要：

1. 在仓库设置中添加 Secrets：
   - `SIGNING_KEY_BASE64` - Base64 编码的签名密钥
   - `KEY_STORE_PASSWORD` - 密钥库密码
   - `KEY_ALIAS` - 密钥别名
   - `KEY_PASSWORD` - 密钥密码

2. 在工作流中添加 Release 编译步骤。

### 设置定时编译

可以设置定时编译，确保代码始终可以正常构建：

```yaml
on:
  schedule:
    - cron: '0 2 * * *'  # 每天凌晨2点编译
```

## 常见问题

### 编译失败怎么办？

1. 检查 Actions 页面的错误日志
2. 确保所有依赖都正确配置
3. 检查 Android SDK 版本兼容性

### 编译时间太长？

1. 缓存已经配置，首次编译后速度会提升
2. 可以考虑使用 GitHub Actions 的自托管运行器

### APK 安装失败？

1. 确保手机开启了 **未知来源应用** 安装权限
2. 检查 Android 版本兼容性
3. Debug APK 仅用于测试，不能发布到应用商店

## 下一步

编译成功后，你可以：

1. 测试应用的基本功能
2. 检查 UI 界面是否正常显示
3. 验证导航功能
4. 测试设置页面的各项功能

如果遇到问题，可以查看编译日志来定位具体原因。