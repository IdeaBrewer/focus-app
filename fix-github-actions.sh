#!/bin/bash

# 修复 GitHub Actions 配置的脚本

echo "正在创建 GitHub Actions 目录..."
mkdir -p .github/workflows

echo "正在创建修复后的 build.yml 文件..."
cat > .github/workflows/build.yml << 'EOF'
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
EOF

echo "修复完成！现在提交并推送..."
git add .github/workflows/build.yml
git commit -m "fix: 修复GitHub Actions配置文件格式错误"
git push origin master

echo "推送完成！请查看 GitHub Actions 页面确认编译状态。"
echo "如果还有问题，请告诉我具体的错误信息。"