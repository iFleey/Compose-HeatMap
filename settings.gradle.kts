pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

dependencyResolutionManagement {
  // The k2 support for wasm is still exceptionally bad, good thing there's a solution for that, it's just stupid!
  // https://youtrack.jetbrains.com/issue/KT-68533/Kotlin-2.0-WasmJs-error-when-using-RepositoriesMode.FAILONPROJECTREPOS
  @Suppress("UnstableApiUsage")
  repositoriesMode = RepositoriesMode.PREFER_SETTINGS

  @Suppress("UnstableApiUsage")
  repositories {
    mavenCentral {
      content {
        excludeGroup("com.yarnpkg")
        excludeGroup("com.github.webassembly")
        excludeGroup("org.nodejs")
      }
    }
    google {
      content {
        excludeGroup("com.yarnpkg")
        excludeGroup("com.github.webassembly")
        excludeGroup("org.nodejs")
      }
    }
    exclusiveContent {
      forRepository {
        ivy("https://nodejs.org/dist/") {
          name = "Node Distributions at $url"
          patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
          metadataSources { artifact() }
          content { includeModule("org.nodejs", "node") }
        }
      }
      filter { includeGroup("org.nodejs") }
    }
    exclusiveContent {
      forRepository {
        ivy("https://github.com/yarnpkg/yarn/releases/download") {
          name = "Yarn Distributions at $url"
          patternLayout { artifact("v[revision]/[artifact](-v[revision]).[ext]") }
          metadataSources { artifact() }
          content { includeModule("com.yarnpkg", "yarn") }
        }
      }
      filter { includeGroup("com.yarnpkg") }
    }
    exclusiveContent {
      forRepository {
        ivy("https://github.com/WebAssembly/binaryen/releases/download") {
          name = "Binaryen Distributions at $url"
          patternLayout { artifact("version_[revision]/[module]-version_[revision]-[classifier].[ext]") }
          metadataSources { artifact() }
          content { includeModule("com.github.webassembly", "binaryen") }
        }
      }
      filter { includeGroup("com.github.webassembly") }
    }
  }
}

rootProject.name = "Compose-HeatMap"
include(":sample")
include(":library")