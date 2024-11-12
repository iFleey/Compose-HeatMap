// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  extra.apply {
    set("compile_sdk", 35)
    set("target_sdk", 35)
  }
}

plugins {
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.composeMultiplatform) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.jetbrainsKotlinAndroid) apply false
  alias(libs.plugins.mavenPublish) apply false
  alias(libs.plugins.compose.compiler) apply false
}