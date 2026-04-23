import com.arkivanov.gradle.setupBinaryCompatibilityValidator
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
// 临时禁用 BCV 以支持 ohosArm64 目标
// setupBinaryCompatibilityValidator()

android {
    namespace = "com.arkivanov.essenty.lifecycle.reaktive"
}

kotlin {
    // 手动添加 ohosArm64 目标（自定义插件不支持）
    ohosArm64()

    setupSourceSets {
        common.main.dependencies {
            implementation(project(":lifecycle"))
            implementation(deps.reaktive.reaktive)
        }
    }
}
