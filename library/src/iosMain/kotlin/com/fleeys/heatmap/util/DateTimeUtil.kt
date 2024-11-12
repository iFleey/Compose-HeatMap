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

import kotlinx.datetime.Month
import platform.Foundation.*

actual fun getLocalizedMonthName(month: Month): String {
  val dateFormatter = NSDateFormatter()
  dateFormatter.dateFormat = "MMMM"
  dateFormatter.locale = NSLocale.currentLocale
  val dateComponents = NSDateComponents()
  dateComponents.month = month.ordinal + 1
  val date = NSCalendar.currentCalendar.dateFromComponents(dateComponents)!!
  return dateFormatter.stringFromDate(date)
}