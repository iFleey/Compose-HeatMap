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

package com.fleeys.heatmap

import com.fleeys.heatmap.model.Heat
import com.fleeys.heatmap.model.HeatWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

internal class HeatAggregator<T>(
  private val heats: List<Heat<T>>
) {
  // Specify the mode of lazy as NONE for single-threaded environments to reduce synchronization overhead and improve performance.
  private val consolidatedHeats by lazy(LazyThreadSafetyMode.NONE) { consolidateHeatsByDate() }
  val weeks by lazy(LazyThreadSafetyMode.NONE) { groupHeatsByWeeks() }

  fun getValueRange(): Pair<Double, Double> {
    val nonZeroHeats = consolidatedHeats.filter { it.value != 0.0 }
    val minIntensity = nonZeroHeats.minByOrNull { it.value }?.value ?: 0.0
    val maxIntensity = nonZeroHeats.maxByOrNull { it.value }?.value ?: 0.0
    return minIntensity to maxIntensity
  }

  private fun consolidateHeatsByDate(): List<Heat<T>> {
    val map = mutableMapOf<LocalDate, Heat<T>>()
    heats.forEach { heat ->
      map.compute(heat.date) { _, existing ->
        if (existing == null) heat else {
          existing.value += heat.value
          existing
        }
      }
    }
    return map.values.toList()
  }

  private fun groupHeatsByWeeks(): List<HeatWeek<T>> {
    return consolidatedHeats
      .sortedByDescending { it.date }
      .groupBy { it.date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) }
      .map { (weekStart, heats) -> HeatWeek(weekStart, heats.toMutableList()) }
  }
}