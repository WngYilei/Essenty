import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupPublication
import com.arkivanov.gradle.setupSourceSets

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform()
setupPublication()

android {
    namespace = "com.arkivanov.essenty.utils.internal"
}

kotlin {
    // 手动添加 ohosArm64 目标（自定义插件不支持）
    ohosArm64()

    setupSourceSets {
        // utils-internal 通常只需要 commonMain，不需要平台特定实现
    }
}
