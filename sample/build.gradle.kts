@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  jvmToolchain(17)

  wasmJs {
    moduleName = "heatmap"
    browser {
      commonWebpackConfig {
        outputFileName = "heatmap.js"
      }
    }
    binaries.executable()
  }

  androidTarget {

    compilerOptions {
      apiVersion.set(KotlinVersion.KOTLIN_2_0)
    }
  }

  jvm("desktop")

  sourceSets {
    val commonMain by getting
    val androidMain by getting
    val desktopMain by getting

    commonMain.dependencies {
      implementation(project(":library"))
      implementation(compose.material)
      implementation(compose.material3)
    }

    androidMain.dependencies {
      implementation(libs.androidx.core.ktx)
      implementation(libs.androidx.appcompat)
      implementation(libs.material)
      implementation(libs.androidx.lifecycle.runtime.ktx)
      implementation(libs.androidx.activity.compose)
      implementation(libs.compose.ui.tooling.preview)
    }

    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
    }

  }
}

android {
  namespace = "com.fleeys.heatmap.sample"

  compileSdk = rootProject.extra["compile_sdk"] as Int

  defaultConfig {
    applicationId = "com.fleeys.heatmap.sample"
    minSdk = 21
    targetSdk = rootProject.extra["target_sdk"] as Int
    versionCode = 1
    versionName = "1.0.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

compose.desktop {
  application {
    mainClass = "com.fleeys.heatmap.sample.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "com.fleeys.heatmap.sample"
      packageVersion = "1.0.0"
    }
  }
}