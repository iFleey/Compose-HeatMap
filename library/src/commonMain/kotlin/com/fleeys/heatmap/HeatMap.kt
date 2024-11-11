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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.fleeys.heatmap.model.Heat
import com.fleeys.heatmap.model.HeatWeek
import com.fleeys.heatmap.style.DaysLabelStyle
import com.fleeys.heatmap.style.HeatFontStyle
import com.fleeys.heatmap.style.HeatMapStyle
import com.fleeys.heatmap.style.HeatStyle
import com.fleeys.heatmap.style.LabelPosition
import com.fleeys.heatmap.style.LabelStyle
import com.fleeys.heatmap.style.MonthsLabelStyle
import java.time.LocalDate
import java.time.Month
import java.util.Locale

@Composable
fun <T> HeatMap(
  modifier: Modifier = Modifier,
  data: List<Heat<T>>,
  style: HeatMapStyle = HeatMapStyle(),
  onHeatClick: (Heat<T>) -> Unit
) {
  val heatAggregator = remember(data) { HeatAggregator(data) }
  val weeks = heatAggregator.weeks
  val valuesRange = heatAggregator.getValueRange()
  var selectedHeat by remember { mutableStateOf<Heat<T>?>(null) }

  Row(modifier) {
    if (style.labelStyle.daysLabelStyle.position == LabelPosition.START) {
      DaysOfWeekLabelsColumn(style.labelStyle)
    }

    LazyRow(
      reverseLayout = style.startFromEnd,
      modifier = Modifier.weight(1f).padding(style.heatMapPadding)
    ) {
      items(weeks) { week ->
        WeekColumn(
          week = week,
          weeks = weeks,
          style = style,
          valuesRange = valuesRange,
          selectedHeat = selectedHeat,
          onHeatClick = {
            onHeatClick(it)
            selectedHeat = it
          }
        )
      }
    }

    if (style.labelStyle.daysLabelStyle.position == LabelPosition.END) {
      DaysOfWeekLabelsColumn(style.labelStyle)
    }
  }
}

@Composable
private fun DaysOfWeekLabelsColumn(style: LabelStyle) {
  val dayNames = remember(style.daysLabelStyle.namesOfWeek) {
    style.daysLabelStyle.namesOfWeek.map { it.replaceFirstChar { char -> char.uppercase() } }
  }

  var offsetY by remember { mutableStateOf(0.dp) }
  val density = LocalDensity.current

  Column(
    Modifier.onGloballyPositioned { coordinates ->
      offsetY = with(density) { coordinates.size.height.toDp() }
    }
  ) {
    DayItem("", style.daysLabelStyle)
  }

  Box(
    Modifier
      .padding(
        start = style.daysLabelStyle.padding.calculateStartPadding(LayoutDirection.Ltr),
        end = style.daysLabelStyle.padding.calculateEndPadding(LayoutDirection.Ltr)
      )
      .offset(
        y = if (style.monthsLabelStyle.position == LabelPosition.TOP) offsetY else -offsetY
      )
      .background(style.daysLabelStyle.color.containerColor, style.daysLabelStyle.shape)
  ) {
    Column { dayNames.forEach { dayName -> DayItem(dayName, style.daysLabelStyle) } }
  }
}

@Composable
private fun <T> WeekColumn(
  week: HeatWeek<T>,
  weeks: List<HeatWeek<T>>,
  style: HeatMapStyle,
  valuesRange: Pair<Double, Double>,
  selectedHeat: Heat<T>? = null,
  onHeatClick: (Heat<T>) -> Unit,
) {
  Column {
    if (style.labelStyle.monthsLabelStyle.position == LabelPosition.TOP) {
      MonthLabel(
        week = week,
        lastMonth = weeks.first().startOfWeek.month,
        style = style.labelStyle.monthsLabelStyle
      )
    }

    (1..7).forEach { dayOfWeek ->
      val heat = week.days.find { it.date.dayOfWeek.value == dayOfWeek }
      val heatColor = heat?.let { getHeatColor(it, valuesRange, style.heatStyle) }
        ?: style.heatStyle.heatColor.inactiveColor

      HeatItem(
        heatColor = heatColor,
        isSelected = heat?.date == selectedHeat?.date,
        dayOfWeek = dayOfWeek,
        week = week,
        style = style.heatStyle,
        onHeatClick = onHeatClick,
      )
    }

    if (style.labelStyle.monthsLabelStyle.position == LabelPosition.BOTTOM) {
      MonthLabel(
        week = week,
        lastMonth = weeks.first().startOfWeek.month,
        style = style.labelStyle.monthsLabelStyle
      )
    }
  }
}

@Composable
private fun DayItem(dayName: String, style: DaysLabelStyle) {
  Box(
    modifier = Modifier
      .height(style.height)
      .padding(style.padding),
    contentAlignment = Alignment.CenterStart
  ) {
    BasicText(
      modifier = Modifier.padding(if (dayName.isEmpty()) 0.dp else 2.dp),
      text = dayName,
      color = { style.color.contentColor },
      style = TextStyle.Default.copy(
        fontSize = style.fontStyle.fontSize,
        fontWeight = style.fontStyle.fontWeight
      )
    )
  }
}

@Composable
private fun <T> MonthLabel(
  week: HeatWeek<T>,
  lastMonth: Month,
  style: MonthsLabelStyle
) {
  val monthText = getMonthText(week, lastMonth)
  Box(
    modifier = Modifier
      .height(style.height)
      .padding(style.padding)
      .aspectRatio(1f)
      .wrapContentSize(align = Alignment.BottomStart, unbounded = true)
  ) {
    BasicText(
      text = monthText,
      color = { style.color.contentColor },
      style = TextStyle.Default.copy(
        fontSize = style.fontStyle.fontSize,
        fontWeight = style.fontStyle.fontWeight
      ),
    )
  }
}

@Composable
private fun <T> HeatItem(
  heatColor: Color,
  isSelected: Boolean,
  style: HeatStyle,
  dayOfWeek: Int,
  week: HeatWeek<T>,
  onHeatClick: (Heat<T>) -> Unit
) {
  val borderWidth by animateDpAsState(
    targetValue = if (isSelected && heatColor != style.heatColor.inactiveColor) {
      style.heatSelectedBorder.borderWidth
    } else {
      style.heatUnselectedBorder.borderWidth
    }
  )

  val borderColor by animateColorAsState(
    targetValue = if (isSelected && heatColor != style.heatColor.inactiveColor) {
      style.heatSelectedBorder.borderColor
    } else {
      style.heatUnselectedBorder.borderColor
    }
  )

  val date = week.startOfWeek.plusDays(dayOfWeek.toLong() - 1)
  val clickedHeat = week.days.find { it.date == date }

  Box(
    modifier = Modifier
      .height(style.height)
      .padding(style.heatPadding)
      .aspectRatio(1f)
      .clip(style.heatShape)
      .background(heatColor)
      .border(width = borderWidth, color = borderColor, shape = style.heatShape)
      .clickable {
        clickedHeat?.let(onHeatClick)
      },
    contentAlignment = Alignment.Center
  ) {
    if (style.showHeatValue) {
      HeatValue(
        date = date,
        fontStyle = style.heatFontStyle,
        backgroundColor = heatColor
      )
    }
  }
}

private fun <T> getMonthText(week: HeatWeek<T>, latestMonth: Month): String {
  val month = week.startOfWeek.month
  val previousMonth = week.startOfWeek.minusWeeks(1).month
  val shouldShowYear = month == latestMonth || month == Month.DECEMBER

  return if (month != previousMonth) {
    month.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())
      .replaceFirstChar { it.uppercase() } +
        if (shouldShowYear) " ${week.startOfWeek.year}" else ""
  } else ""
}

@Composable
private fun HeatValue(date: LocalDate, fontStyle: HeatFontStyle, backgroundColor: Color) {
  val textColor =
    if (backgroundColor.luminance() > 0.5) fontStyle.fontDarkColor else fontStyle.fontLightColor
  BasicText(
    text = date.dayOfMonth.toString(),
    color = { textColor },
    style = TextStyle.Default.copy(
      fontSize = fontStyle.fontSize,
      fontWeight = fontStyle.fontWeight
    )
  )
}

private fun <T> getHeatColor(
  heat: Heat<T>,
  valuesRange: Pair<Double, Double>,
  style: HeatStyle
): Color {
  val fraction =
    ((heat.value - valuesRange.first) / (valuesRange.second - valuesRange.first)).coerceIn(
      0.0,
      1.0
    ).toFloat()
  return lerp(style.heatColor.activeLowestColor, style.heatColor.activeHighestColor, fraction)
}