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

internal val defaultNameOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Immutable
data class DaysLabelStyle(
  val height: Dp = 30.dp,
  val position: LabelPosition = LabelPosition.END,
  val shape: Shape = RoundedCornerShape(4.dp),
  val namesOfWeek: List<String> = defaultNameOfWeek,
  val fontStyle: DaysLabelFontStyle = DaysLabelFontStyle(),
  val color: DaysLabelColor = DaysLabelColor(),
  val padding: PaddingValues = PaddingValues(2.dp)
)

@Immutable
data class DaysLabelFontStyle(
  val fontSize: TextUnit = 14.sp,
  val fontWeight: FontWeight = FontWeight.Normal,
)

@Immutable
data class DaysLabelColor(
  val contentColor: Color = Color(0xff787878),
  val containerColor: Color = Color(0xffdedede),
)