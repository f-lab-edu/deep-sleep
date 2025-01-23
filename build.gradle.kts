// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "2.1.0" // Kotlin JVM 플러그인
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.parcelize) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2") // Android Gradle Plugin
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1") // Hilt 플러그인
        classpath("org.jetbrains.kotlin.plugin.parcelize:org.jetbrains.kotlin.plugin.parcelize.gradle.plugin:2.1.20-Beta1") // Parcelize Plugin
    }
}
