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

package com.fleeys.heatmap.style

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class HeatStyle(
  val height: Dp = 30.dp,
  val heatColor: HeatColor = HeatColor(),
  val heatSelectedBorder: HeatBorder = HeatBorder(),
  val heatUnselectedBorder: HeatBorder = HeatBorder().copy(
    borderWidth = 0.dp, borderColor = Color.Transparent
  ),
  val heatFontStyle: HeatFontStyle = HeatFontStyle(),
  val heatPadding: PaddingValues = PaddingValues(1.dp),
  val heatShape: Shape = RoundedCornerShape(2.dp),
  val showHeatValue: Boolean = true
)

@Immutable
data class HeatColor(
  val inactiveColor: Color = Color(0x52a7a7a7),
  val activeLowestColor: Color = Color(0xff0e4429),
  val activeHighestColor: Color = Color(0xff39d353)
)

@Immutable
data class HeatFontStyle(
  val fontLightColor: Color = Color.White,
  val fontDarkColor: Color = Color(0xff787878),
  val fontSize: TextUnit = 14.sp,
  val fontWeight: FontWeight = FontWeight.Normal,
)

@Immutable
data class HeatBorder(
  val borderWidth: Dp = 3.dp,
  val borderColor: Color = Color(0xff787878),
)