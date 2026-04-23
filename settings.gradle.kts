dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            from(files("deps.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://mirrors.tencent.com/nexus/repository/maven-tencent")
        maven("https://mirrors.tencent.com/nexus/repository/maven-public")
        maven("https://repo.huawei.com/repository/maven/")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.toString() == "com.arkivanov.gradle.setup") {
                // 优先使用本地 mavenLocal 中的插件版本（支持 ohosArm64）
                val localPlugin = file("${System.getProperty("user.home")}/.m2/repository/com/arkivanov/gradle/gradle-setup-plugin/0.0.1")
                if (localPlugin.exists()) {
                    useModule("com.arkivanov.gradle:gradle-setup-plugin:0.0.1")
                } else {
                    useModule("com.github.arkivanov:gradle-setup-plugin:4ae41e7b6a")
                }
            }
        }
    }

    plugins {
        id("com.arkivanov.gradle.setup")
    }
}

if (!startParameter.projectProperties.containsKey("check_publication")) {
    include(":utils-internal")
    include(":lifecycle")
    include(":lifecycle-coroutines")
    include(":lifecycle-reaktive")
    include(":state-keeper")
    include(":state-keeper-benchmarks")
    include(":instance-keeper")
    include(":back-handler")
} else {
    include(":tools:check-publication")
}
