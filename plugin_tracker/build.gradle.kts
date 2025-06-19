plugins {
    id("java-library")
    id("java-gradle-plugin")
    id("maven-publish")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

val MAVEN_GROUP_ID = "cn.losefeather.plugins"
val MAVEN_ARTIFACT_ID = "tracker"
val MAVEN_VERSION = "1.0.0"
val MAVEN_NAME = "tracker"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

gradlePlugin {
    plugins {
        create("tracker") {
            id = "cn.losefeather.tracker"
            implementationClass = "cn.losefaether.plugin_tracker.TrackerPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>(MAVEN_NAME) {
            groupId = MAVEN_GROUP_ID
            artifactId = MAVEN_ARTIFACT_ID
            version = MAVEN_VERSION
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir(MAVEN_NAME))
            println("Setting maven url is $url")
        }
    }

}

dependencies {
    implementation(gradleApi())
    implementation(libs.gradle)
    implementation("org.ow2.asm:asm-commons:9.5") // 添加此行
}