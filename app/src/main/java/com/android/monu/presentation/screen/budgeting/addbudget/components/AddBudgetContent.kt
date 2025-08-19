package com.android.monu.presentation.screen.budgeting.addbudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.TextAmountInputField
import com.android.monu.presentation.components.TextDateInputField
import com.android.monu.presentation.components.TextInputField
import com.android.monu.presentation.utils.Cycle
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.DateHelper

@Composable
fun AddBudgetContent(
    budgetState: AddBudgetContentState,
    budgetActions: AddBudgetContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = stringResource(R.string.category),
            value = if (budgetState.category == 0) "" else
                stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetState.category)),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_transaction_category),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { budgetActions.onNavigateToCategory() }
                )
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            isEnable = false
        )
        TextAmountInputField(
            title = stringResource(R.string.maximum_amount),
            value = budgetState.maxAmountFormat,
            onValueChange = { budgetActions.onAmountChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        CycleFilterField(
            cycles = listOf(Cycle.MONTHLY, Cycle.WEEKLY, Cycle.CUSTOM),
            selectedCycle = budgetState.period,
            onCycleChange = { budgetActions.onPeriodChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.start_date),
            value = DateHelper.formatDateToReadable(budgetState.startDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_budgeting_start_date),
            isEnable = budgetState.period == Cycle.CUSTOM,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetState.period == Cycle.CUSTOM) {
                            budgetActions.onStartDateClick()
                        }
                    }
                )
                .padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.end_date),
            value = DateHelper.formatDateToReadable(budgetState.endDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_budgeting_end_date),
            isEnable = budgetState.period == Cycle.CUSTOM,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetState.period == Cycle.CUSTOM) {
                            budgetActions.onEndDateClick()
                        }
                    }
                )
                .padding(horizontal = 16.dp)
        )
        TextSwitchField(
            isOverflowAllowed = budgetState.isOverflowAllowed,
            onOverflowAllowedChange = { budgetActions.onOverflowAllowedChange(it) },
            isAutoUpdate = budgetState.isAutoUpdate,
            onAutoUpdateChange = { budgetActions.onAutoUpdateChange(it) },
            budgetingPeriod = budgetState.period,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = { budgetActions.onAddNewBudget(budgetState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.add),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun CycleFilterField(
    cycles: List<Int>,
    selectedCycle: Int,
    onCycleChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.cycle),
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
        ) {
            cycles.forEach { cycle ->
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(
                            if (cycle == selectedCycle) MaterialTheme.colorScheme.primary else
                                MaterialTheme.colorScheme.background,
                        )
                        .clickable { onCycleChange(cycle) }
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(DatabaseCodeMapper.toCycle(cycle)),
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (cycle == selectedCycle) MaterialTheme.colorScheme.onPrimary else
                                MaterialTheme.colorScheme.onBackground,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun TextSwitchField(
    isOverflowAllowed: Boolean,
    onOverflowAllowedChange: (Boolean) -> Unit,
    isAutoUpdate: Boolean,
    onAutoUpdateChange: (Boolean) -> Unit,
    budgetingPeriod: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        TextWithSwitch(
            text = stringResource(R.string.budgeting_overflow_allowed),
            checked = isOverflowAllowed,
            onCheckedChange = { onOverflowAllowedChange(it) },
            isEnable = true
        )
        TextWithSwitch(
            text = stringResource(R.string.bugdeting_auto_update),
            checked = isAutoUpdate,
            onCheckedChange = { onAutoUpdateChange(it) },
            isEnable = budgetingPeriod != Cycle.CUSTOM
        )
    }
}

@Composable
fun TextWithSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEnable: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            } else {
                null
            },
            enabled = isEnable,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.background,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.background,
                uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledUncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                disabledUncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledUncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

data class AddBudgetContentState(
    val category: Int,
    val maxAmount: Long,
    val maxAmountFormat: TextFieldValue,
    val period: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)

interface AddBudgetContentActions {
    fun onNavigateToCategory()
    fun onAmountChange(maxAmountFormat: TextFieldValue)
    fun onPeriodChange(period: Int)
    fun onStartDateClick()
    fun onEndDateClick()
    fun onOverflowAllowedChange(isOverflowAllowed: Boolean)
    fun onAutoUpdateChange(isAutoUpdate: Boolean)
    fun onAddNewBudget(budgetState: AddBudgetContentState)
}