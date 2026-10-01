# 一键锁屏

给 Android 9 及以上手机准备的极简锁屏应用。点击桌面图标后，应用调用系统的 `DevicePolicyManager.lockNow()`，随后关闭自己的页面。没有后台服务、无障碍服务、开机启动、网络权限或广告。

## 安装和使用

1. 将 APK 安装到手机，并将“一键锁屏”图标放在桌面容易点击的位置。
2. 第一次点击图标时，按系统提示启用设备管理权限。应用仅申请“锁定屏幕”这一项策略。授权成功后会立即锁屏。
3. 以后每次点击图标都会立即锁屏。点亮屏幕不会再次触发应用。

系统可能要求使用 PIN、图案或密码解锁，而不能只用指纹或人脸。这是 Android 设备管理锁屏接口的行为；请先在爷爷的手机上试一次，确认解锁方式方便他使用。

若要卸载，先在手机的“设置 → 安全 → 设备管理应用”（不同荣耀系统的名称可能略有不同）中停用“一键锁屏”，再正常卸载。

## 构建

仓库推送到 `main` 后由 GitHub Actions 构建签名正式版，无需在电脑上安装 Android 构建环境。工作流使用 JDK 17、Android SDK 35、Android Gradle Plugin 9.0.1 和 Gradle 9.2.1。构建结果可从 Actions 的 `one-tap-lock-apk` artifact 下载。仓库的 Actions Secrets 中需要 `SIGNING_KEYSTORE_BASE64`（密钥库文件的 Base64 内容）和 `SIGNING_PASSWORD`；密钥库文件本身不得提交到公开仓库。

若已有本地环境、签名密钥和对应环境变量，也可以运行：

```sh
gradle assembleRelease
```

生成的可安装 APK 位于 `app/build/outputs/apk/release/app-release.apk`。工程只使用 Android 平台 API，没有第三方运行时依赖。
