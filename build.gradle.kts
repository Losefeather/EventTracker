// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()

        subprojects.forEach { project ->
            if (project.name == "app") {
                println("主项目：" + project.name)
            } else if (project.name.startsWith("lib")) {
                println("子项目：" + project.name)
            } else if (project.name.startsWith("plugin")) {
                println("插件：" + project.name)
            }

        }
//
        maven { setUrl("https://jitpack.io") }
    }

    dependencies {
        classpath(libs.gradle)
        classpath("cn.losefeather.plugins:tracker:1.0.0")
    }
}
