@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.mavenPublish)
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
    publishLibraryVariants("release")

    compilerOptions {
      apiVersion.set(KotlinVersion.KOTLIN_2_0)
    }
  }

  jvm("desktop")

  sourceSets {
    val desktopMain by getting
    val desktopTest by getting

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
    }
    commonMain.dependencies {
      implementation(compose.foundation)
      implementation(compose.ui)
    }
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
    desktopTest.dependencies {
      implementation(compose.desktop.uiTestJUnit4)
    }
  }

}

android {
  namespace = "com.fleeys.heatmap.core"
  compileSdk = rootProject.extra["compile_sdk"] as Int

  defaultConfig {
    minSdk = 21

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  dependencies {
    debugImplementation(libs.compose.ui.tooling)
  }
  
}