package com.android.monu.presentation.screen.reports.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.YearFilterButton
import com.android.monu.presentation.screen.reports.ReportsFilterCallbacks
import com.android.monu.presentation.screen.reports.ReportsFilterState
import com.android.monu.ui.theme.LightGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.util.debouncedClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsAppBar(
    filterState: ReportsFilterState,
    filterCallbacks: ReportsFilterCallbacks
) {
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    var selectedYearFilter by remember { mutableIntStateOf(filterState.selectedYear) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.monthly_reports),
                modifier = Modifier,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
        },
        actions = {
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
                    offset = DpOffset(x = (-8).dp, y = 4.dp)
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
                                filterCallbacks.onYearFilterSelect(year)
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
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = LightGrey)
    )
}

@Preview(showBackground = true)
@Composable
fun ReportsAppBarPreview() {
    ReportsAppBar(
        filterState = ReportsFilterState(
            selectedYear = 2025,
            availableYears = listOf(2025, 2024, 2023)
        ),
        filterCallbacks = ReportsFilterCallbacks(
            onFilterClick = { },
            onYearFilterSelect = { }
        )
    )
}

