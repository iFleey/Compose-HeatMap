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
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

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
      val existing = map[heat.date]
      if (existing == null) {
        map[heat.date] = heat
      } else {
        existing.value += heat.value
      }
    }
    return map.values.toList()
  }

  private fun groupHeatsByWeeks(): List<HeatWeek<T>> {
    return consolidatedHeats
      .sortedByDescending { it.date }
      .groupBy { it.date.startOfWeek() }
      .map { (weekStart, heats) -> HeatWeek(weekStart, heats.toMutableList()) }
  }
}

// Extension function to find the start of the week (Monday) for kotlinx.datetime.LocalDate
private fun LocalDate.startOfWeek(): LocalDate {
  val currentDayOfWeek = this.dayOfWeek
  val daysToSubtract =
    (currentDayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).takeIf { it >= 0 } ?: 6
  return this.minus(daysToSubtract.toLong(), kotlinx.datetime.DateTimeUnit.DAY)
}