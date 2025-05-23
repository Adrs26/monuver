package com.android.monu.presentation.screen.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.presentation.components.YearFilterButton
import com.android.monu.presentation.screen.analytics.AnalyticsFilterCallbacks
import com.android.monu.presentation.screen.analytics.AnalyticsFilterState
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.debouncedClickable

@Composable
fun AnalyticsChartTopBar(
    title: String,
    chartType: Int,
    filterState: AnalyticsFilterState,
    filterCallbacks: AnalyticsFilterCallbacks,
    modifier: Modifier = Modifier
) {
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    var selectedYearFilter by remember {
        mutableIntStateOf(
            if (chartType == 1) filterState.barChartSelectedYear else
                filterState.pieChartSelectedYear
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
        Box {
            YearFilterButton(
                year = selectedYearFilter.toString(),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .debouncedClickable {
                        dropdownMenuExpanded = !dropdownMenuExpanded
                        filterCallbacks.onFilterClick()
                    }
            )
            DropdownMenu(
                expanded = dropdownMenuExpanded,
                onDismissRequest = { dropdownMenuExpanded = false },
                modifier = Modifier
                    .background(color = Color.White)
                    .heightIn(max = 160.dp),
                offset = DpOffset(x = (-32).dp, y = 4.dp)
            ) {
                filterState.availableYears.forEach { year ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = year.toString(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = interFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            )
                        },
                        onClick = {
                            if (chartType == 1) {
                                filterCallbacks.onBarChartYearFilterSelect(year)
                            } else {
                                filterCallbacks.onPieChartYearFilterSelect(year)
                            }
                            selectedYearFilter = year
                            dropdownMenuExpanded = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (year == selectedYearFilter) Color.Black else Color.White
                            )
                        }
                    )
                }
            }
        }
    }
}