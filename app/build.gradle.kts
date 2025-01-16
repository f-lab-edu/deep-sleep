import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}
android {
    namespace = "com.flab.deepsleep"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.flab.deepsleep"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val unsplashAccessKey = localProperties.getProperty("UNSPLASH_ACCESS_KEY") ?: ""
        buildConfigField("String", "UNSPLASH_ACCESS_KEY", "\"$unsplashAccessKey\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}
dependencies {
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.hilt.android)
    implementation(libs.hilt.converter)
    implementation(libs.logging.interceptor)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.glide)
    implementation(libs.json)
    implementation(libs.timber)
    implementation(libs.paging)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.okhttp)
    implementation(libs.okhttp.profiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.viewpager2)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
hilt {
    enableAggregatingTask = false
}