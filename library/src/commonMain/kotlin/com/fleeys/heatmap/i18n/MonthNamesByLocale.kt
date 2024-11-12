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

package com.fleeys.heatmap.i18n

open class MonthNames(
  private val names: List<String>
) {
  init {
    require(names.size == 12) { "Month names must contain exactly 12 elements" }
    names.indices.forEach { ix ->
      require(names[ix].isNotEmpty()) { "A month name cannot be empty" }
      for (ix2 in 0 until ix) {
        require(names[ix] != names[ix2]) {
          "Month names must be unique, but '${names[ix]}' was repeated"
        }
      }
    }
  }

  // Companion object with predefined month names for different locales
  companion object {
    val ENGLISH_FULL = MonthNames(
      listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
      )
    )

    val ENGLISH_ABBREVIATED = MonthNames(
      listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
      )
    )

    val CHINESE_FULL = MonthNames(
      listOf(
        "一月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "十一月", "十二月"
      )
    )

    val CHINESE_ABBREVIATED = MonthNames(
      listOf(
        "1月", "2月", "3月", "4月", "5月", "6月",
        "7月", "8月", "9月", "10月", "11月", "12月"
      )
    )
  }

  /**
   * Returns a copy of this [MonthNames] with any of the months modified.
   */
  fun copy(
    january: String = names[0],
    february: String = names[1],
    march: String = names[2],
    april: String = names[3],
    may: String = names[4],
    june: String = names[5],
    july: String = names[6],
    august: String = names[7],
    september: String = names[8],
    october: String = names[9],
    november: String = names[10],
    december: String = names[11]
  ): MonthNames {
    return MonthNames(
      listOf(
        january, february, march, april, may, june,
        july, august, september, october, november, december
      )
    )
  }

  /**
   * Returns the month name for the given month index (0 for January, 11 for December).
   */
  fun getMonthName(index: Int): String {
    require(index in 0..11) { "Index must be between 0 and 11" }
    return names[index]
  }

  override fun toString(): String = "FriendlyMonthNames(${names.joinToString(", ")})"

  override fun equals(other: Any?): Boolean = other is MonthNames && names == other.names

  override fun hashCode(): Int = names.hashCode()
}