# GitHub Actions 工作流配置

这个目录包含 GitHub Actions 的工作流配置文件，用于自动编译 Android APK。

## 文件说明

- `build.yml` - 自动编译 Debug 和 Release APK
- `test.yml` - 运行单元测试和集成测试
- `release.yml` - 创建 GitHub Release 并上传 APK

## 使用方法

1. 这些文件会自动在每次 push 到 master/main 分支时触发
2. 编译完成后，APK 文件会作为 artifacts 上传
3. 你可以在 GitHub Actions 页面下载编译好的 APK

## 环境变量配置（可选）

如果要编译签名版的 Release APK，需要在 GitHub 仓库的 Settings > Secrets and variables > Actions 中添加以下密钥：

- `SIGNING_KEY_BASE64` - Base64 编码的签名密钥文件
- `KEY_STORE_PASSWORD` - 密钥库密码
- `KEY_ALIAS` - 密钥别名
- `KEY_PASSWORD` - 密钥密码

## 快速开始

1. 将这些文件复制到你的 GitHub 仓库的 `.github/workflows/` 目录
2. 提交并推送到 GitHub
3. 在 GitHub Actions 页面查看编译进度
4. 编译完成后下载 APK 文件安装测试