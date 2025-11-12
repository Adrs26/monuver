package com.android.monuver.ui.feature.screen.settings.export.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.domain.model.ExportState
import com.android.monuver.ui.feature.components.StaticTextInputField
import com.android.monuver.ui.feature.components.TextDateInputField
import com.android.monuver.ui.feature.components.TextInputField
import com.android.monuver.ui.feature.components.TextWithSwitch
import com.android.monuver.utils.DateHelper

@Composable
fun ExportContent(
    exportState: ExportState,
    exportActions: ExportContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = stringResource(R.string.report_title),
            value = exportState.title,
            onValueChange = exportActions::onTitleChange,
            placeholderText = stringResource(R.string.enter_report_title),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextInputField(
            title = stringResource(R.string.report_username),
            value = exportState.username,
            onValueChange = exportActions::onUserNameChange,
            placeholderText = stringResource(R.string.enter_report_username),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.report_start_period),
            value = DateHelper.formatDateToReadable(exportState.startDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_report_start_period),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = exportActions::onStartDateClick
                )
                .padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.report_end_period),
            value = DateHelper.formatDateToReadable(exportState.endDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_report_end_period),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = exportActions::onEndDateClick
                )
                .padding(horizontal = 16.dp)
        )
        StaticTextInputField(
            title = stringResource(R.string.report_format),
            value = "PDF",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        SortDateTypeRadioGroupField(
            selectedSortType = exportState.sortType,
            onSortTypeSelect = exportActions::onSortTypeChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            TextWithSwitch(
                text = stringResource(R.string.group_income_expense),
                checked = exportState.isIncomeExpenseGrouped,
                onCheckedChange = exportActions::onIncomeExpenseGroupedChange,
                isEnable = true
            )
            TextWithSwitch(
                text = stringResource(R.string.include_transfer_transaction),
                checked = exportState.isTransferIncluded,
                onCheckedChange = exportActions::onTransferIncludedChange,
                isEnable = true
            )
        }
    }
}

@Composable
private fun SortDateTypeRadioGroupField(
    selectedSortType: Int,
    onSortTypeSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.sort_by),
            modifier = Modifier.padding(bottom = 12.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSortType == 1,
                    onClick = { onSortTypeSelect(1) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = stringResource(R.string.oldest_date),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSortType == 2,
                    onClick = { onSortTypeSelect(2) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = stringResource(R.string.newest_date),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}

interface ExportContentActions {
    fun onTitleChange(title: String)
    fun onUserNameChange(username: String)
    fun onStartDateClick()
    fun onEndDateClick()
    fun onSortTypeChange(sortType: Int)
    fun onIncomeExpenseGroupedChange(isIncomeExpenseGrouped: Boolean)
    fun onTransferIncludedChange(isTransferIncluded: Boolean)
}