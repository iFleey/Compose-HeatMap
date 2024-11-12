import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.mavenPublish)
}

kotlin {
  jvmToolchain(17)

  androidTarget {
    publishLibraryVariants("release")

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
      apiVersion.set(KotlinVersion.KOTLIN_2_0)
      languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    moduleName = "heatmap"
    browser {
      binaries.executable()
      commonWebpackConfig {
        outputFileName = "heatmap.js"
        devServer = KotlinWebpackConfig.DevServer(
          static = mutableListOf("build/processedResources/wasmJs/main")
        )
      }
    }
  }

  jvm("desktop")

  sourceSets {
    val desktopMain by getting
    val desktopTest by getting
    val wasmJsMain by getting

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)
    }
    commonMain.dependencies {
      implementation(compose.foundation)
      implementation(compose.ui)
      implementation(libs.kotlinx.datetime)
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

tasks.register<Copy>("copyJsResources") {
  from("src/wasmJsMain/resources")
  into("build/processedResources/wasmJs/main")
  duplicatesStrategy = DuplicatesStrategy.INCLUDE // 设置重复处理策略
}

tasks.getByName("wasmJsProcessResources").dependsOn("copyJsResources")