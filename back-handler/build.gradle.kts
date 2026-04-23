import com.arkivanov.gradle.bundle
import com.arkivanov.gradle.dependsOn
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
    namespace = "com.arkivanov.essenty.backhandler"
}

kotlin {
    // 手动添加 ohosArm64 目标(自定义插件不支持)
    ohosArm64()

    setupSourceSets {
        val android by bundle()
        val ohosArm64 by bundle()

        ohosArm64 dependsOn common

        common.main.dependencies {
            implementation(project(":utils-internal"))
        }

        android.main.dependencies {
            implementation(deps.androidx.activity.activityKtx)
        }
    }
}
