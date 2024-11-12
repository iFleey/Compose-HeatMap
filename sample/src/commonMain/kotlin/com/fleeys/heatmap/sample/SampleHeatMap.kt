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
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fleeys.heatmap.HeatMap
import com.fleeys.heatmap.model.Heat
import com.fleeys.heatmap.style.HeatMapStyle
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random


@Composable
fun SampleHeatMap() {
  var heatMapStyle by remember { mutableStateOf<HeatMapStyle?>(null) }
  val toggleStyle = { heatMapStyle = if (heatMapStyle == null) CustomHeatMapStyle else null }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
      .padding(16.dp)
  ) {
    HeatMap(
      style = heatMapStyle ?: HeatMapStyle(),
      data = generateHeats(),
    ) { println("Clicked: $it") }

    Button(
      onClick = toggleStyle,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(16.dp)
    ) {
      Text("Toggle Style")
    }
  }
}

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