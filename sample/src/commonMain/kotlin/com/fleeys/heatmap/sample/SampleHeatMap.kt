/*
 * Copyright (c) 2024-present. Fleey
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fleeys.heatmap.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fleeys.heatmap.HeatMap
import com.fleeys.heatmap.model.Heat
import com.fleeys.heatmap.style.HeatMapStyle
import java.time.LocalDate
import kotlin.random.Random

@Composable
fun SampleHeatMap() {
  Box(
    Modifier
      .fillMaxSize()
      .background(Color.Black)
      .padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    HeatMap(
      style = HeatMapStyle(),
      data = generateHeats()
    ) {
      println("Clicked: $it")
    }
  }
}

private fun generateHeats(): List<Heat<Unit>> {
  val heats = mutableListOf<Heat<Unit>>()
  val startDate = LocalDate.of(2022, 11, 11)
  val curDate = LocalDate.now()

  var currentDate = startDate
  while (!currentDate.isAfter(curDate)) {
    val value = Random.nextDouble(0.00, 32.00)
    heats.add(Heat(currentDate, value))
    currentDate = currentDate.plusDays(1)
  }

  return heats
}