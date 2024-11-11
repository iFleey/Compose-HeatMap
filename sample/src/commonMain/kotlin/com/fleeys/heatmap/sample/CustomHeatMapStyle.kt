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

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import com.fleeys.heatmap.style.HeatColor
import com.fleeys.heatmap.style.HeatMapStyle
import com.fleeys.heatmap.style.HeatStyle

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