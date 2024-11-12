# Compose-HeatMap

[![Maven Central](https://img.shields.io/maven-central/v/com.fleeys/heatmap)](https://central.sonatype.com/artifact/com.fleeys/heatmap) ![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android) ![Desktop](https://img.shields.io/badge/Platform-Desktop-8A2BE2.svg?logo=openjdk) ![WasmJS](https://img.shields.io/badge/Platform-WASM%20%2F%20JS-yellow.svg?logo=javascript)

Effortlessly create GitHub-style heatmaps in Jetpack Compose—perfect for visualizing a variety of time-based data patterns.

## Preview

| Platform |                     Default Style                      |                     Custom Style                     |
| :------: | :----------------------------------------------------: | :--------------------------------------------------: |
| Android  | ![Default-Style](./images/Android/preview-default.jpg) | ![Custom-Style](./images/Android/preview-custom.jpg) |
| Desktop  | ![Default-Style](./images/Desktop/preview-default.png) | ![Custom-Style](./images/Desktop/preview-custom.png) |
|  WasmJs  | ![Default-Style](./images/WasmJs/preview-default.png)  | ![Default-Style](./images/WasmJs/preview-custom.png) |

## Usage

This project is based on `kotlinx-datetime` for LocalDate-related operations, not `java.time.*`!

refer to [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) to add dep.

> build.gradle.kts

```kotlin
implementation("com.fleeys:heatmap:1.0.5")
```

> build.gradle

```groovy
implementation 'com.fleeys:heatmap:1.0.5'
```

## Overview

```kotlin
// ../library/src/commonMain/kotlin/com/fleeys/heatmap/HeatMap.kt

@Composable
fun <T> HeatMap(
  modifier: Modifier = Modifier,
  data: List<Heat<T>>,
  style: HeatMapStyle = HeatMapStyle(),
  scrollState: LazyListState = rememberLazyListState(),
  onScrolledToTop: (() -> Unit)? = null,
  onScrolledToBottom: (() -> Unit)? = null,
  onHeatClick: (Heat<T>) -> Unit,
)
```

## Features


> [!IMPORTANT]  
> The project is in the experimental phase. All APIs can change incompatibly or be dropped without the deprecation cycle!

### Easy to use

Works right out of the box without much setup.

```kotlin
// ../sample/src/commonMain/com/fleeys/heatmap/sample/SampleHeatMap.kt

@Composable
fun SampleHeatMap() {
  val scrollState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  var heatMapStyle by remember { mutableStateOf<HeatMapStyle?>(null) }
  val toggleStyle = { heatMapStyle = if (heatMapStyle == null) CustomHeatMapStyle else null }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
      .padding(16.dp)
  ) {
    HeatMap(
      data = generateHeats(),
      scrollState = scrollState,
      style = heatMapStyle ?: HeatMapStyle(),
      onScrolledToTop = { println("Scrolled to Top") },
      onScrolledToBottom = { println("Scrolled to Bottom") }
    ) { println("Clicked: $it") }

    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Button(
        onClick = toggleStyle,
      ) {
        Text("Toggle Style")
      }
      Button(onClick = {
        coroutineScope.launch {
          scrollState.animateScrollToItem(0)
        }
      }) {
        Text("Scroll to Top")
      }

      Button(onClick = {
        coroutineScope.launch {
          scrollState.animateScrollToItem(scrollState.layoutInfo.totalItemsCount - 1)
        }
      }) {
        Text("Scroll to Bottom")
      }
    }
  }
}

// fake data
private fun generateHeats(): List<Heat<Unit>> {
  val startDate = LocalDate(2022, 11, 11)
  val curDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

  return generateSequence(startDate) { date ->
    if (date < curDate) date + DatePeriod(days = 1) else null
  }.map { date ->
    val value = Random.nextDouble(0.0, 32.0)
    Heat<Unit>(date, value)
  }.toList()
}
```

### Highly customizable

Almost everything you see can be manipulated.

```kotlin
// ../library/src/commonMain/kotlin/com/fleeys/heatmap/style/HeatMapStyle.kt

@Immutable
data class HeatMapStyle(
  val heatStyle: HeatStyle = HeatStyle(),
  val labelStyle: LabelStyle = LabelStyle(),
  val heatMapPadding: PaddingValues = PaddingValues(0.dp),
  val startFromEnd: Boolean = true
)

// Sample Custom Style
// ../sample/src/commonMain/kotlin/com/fleeys/heatmap/sample/CustomHeatMapStyle.kt
internal val CustomHeatMapStyle = HeatMapStyle().copy(
  heatStyle = HeatStyle().copy(
    heatColor = HeatColor().copy(
      activeLowestColor = Color(0xff212f57),
      activeHighestColor = Color(0xff456de3),
    ),
    heatShape = CircleShape,
  ),
  startFromEnd = false
)
```

### Interactivity

Not just present, but interact.

- Click
- Scroll
- Reaching the top or pulling back at the end

### Generic data support

The operation is richer.

```kotlin
// ../library/src/commonMain/kotlin/com/fleeys/heatmap/HeatMap.kt

import kotlinx.datetime.LocalDate

data class Heat<T>(
  val date: LocalDate,
  var value: Double,
  var data: T? = null
)
```

## Supportive

This project is built on Compose in an attempt to adapt to the Compose Multiplatform.

The currently adapted platforms are listed below:

| Platform |      State       |                            Sample                            | Note |
| :------: | :--------------: | :----------------------------------------------------------: | :--: |
| Android  |        ✅         | [Android-Sample](./sample/src/androidMain/kotlin/com/fleeys/heatmap/sample/MainActivity.kt) |      |
| Desktop  |        ✅         | [Desktop-Sample](./sample/src/desktopMain/kotlin/com/fleeys/heatmap/sample/Main.kt) |      |
|  WasmJs  | :expressionless: |                             ...                              | (1)  |

1. Unfortunately, due to the fact that I don't know much about it, the realization is less than ideal. If you can help me refine it, quite welcome to contribute!

## Contribution

Feel free to submit an issue if you have any feedback or suggestions!

The project is quite happy to receive your contributions, and it will be much more robust with your help!

I hope you like it, and if you think it's good, feel free to give a :star: !

## License

```
Copyright (c) 2024-present. Fleey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```