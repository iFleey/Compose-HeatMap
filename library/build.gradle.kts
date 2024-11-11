plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  jvmToolchain(17)

  androidTarget {
    publishLibraryVariants("release")
    compilations.all {
      kotlinOptions {
        jvmTarget = "17"
      }
    }
  }

  jvm("desktop")

  sourceSets {
    val desktopMain by getting

    androidMain.dependencies {
      implementation(libs.androidx.activity.compose)

    }
    commonMain.dependencies {
      implementation(libs.androidx.compose.bom)
      implementation(compose.foundation)
      implementation(compose.ui)
    }
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
    val desktopTest by getting {
      dependencies {
        implementation(compose.desktop.uiTestJUnit4)
      }
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
//  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  dependencies {
    debugImplementation(libs.compose.ui.tooling)
  }
}