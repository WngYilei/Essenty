import com.arkivanov.gradle.bundle
import com.arkivanov.gradle.dependsOn
import com.arkivanov.gradle.setupBinaryCompatibilityValidator
import com.arkivanov.gradle.setupMultiplatform
import com.arkivanov.gradle.setupPublication
import com.arkivanov.gradle.setupSourceSets

plugins {
    id("kotlin-multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.arkivanov.gradle.setup")
}

setupMultiplatform()
setupPublication()
// 临时禁用 BCV 以支持 ohosArm64 目标
// setupBinaryCompatibilityValidator()

android {
    namespace = "com.arkivanov.essenty.statekeeper"
}

kotlin {
    // 手动添加 ohosArm64 目标（自定义插件不支持）
    ohosArm64()

    setupSourceSets {
        val java by bundle()
        val nonJava by bundle()
        val android by bundle()

        java dependsOn common
        javaSet dependsOn java
        nonJava dependsOn common
        (allSet - javaSet) dependsOn nonJava
        // ohosArm64 自动继承 nonJava，因为它是 native 目标

        common.main.dependencies {
            implementation(project(":utils-internal"))
            api(deps.jetbrains.kotlinx.kotlinxSerializationCore)
            implementation(deps.jetbrains.kotlinx.kotlinxSerializationJson)
        }

        android.main.dependencies {
            implementation(deps.androidx.savedstate.savedstateKtx)
            implementation(deps.androidx.lifecycle.lifecycleRuntime)
        }

        android.test.dependencies {
            implementation(deps.robolectric.robolectric)
        }

        // 手动配置 ohosArm64 源集依赖
        // 注意：kotlinx-serialization 可能不支持 ohos_arm64 目标
        // 如果编译失败，可能需要使用 alias 或自定义编译的 kotlinx 库
        sourceSets {
            getByName("ohosArm64Main").dependsOn(getByName("nonJavaMain"))
        }
    }
}
