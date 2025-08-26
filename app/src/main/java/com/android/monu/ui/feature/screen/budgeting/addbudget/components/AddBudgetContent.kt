package com.android.monu.ui.feature.screen.budgeting.addbudget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.ui.feature.components.CycleFilterField
import com.android.monu.ui.feature.components.TextAmountInputField
import com.android.monu.ui.feature.components.TextDateInputField
import com.android.monu.ui.feature.components.TextInputField
import com.android.monu.ui.feature.screen.budgeting.components.BudgetTextSwitchField
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.ui.feature.utils.DateHelper

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
                    onClick = budgetActions::onNavigateToCategory
                )
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            isEnable = false
        )
        TextAmountInputField(
            title = stringResource(R.string.maximum_amount),
            value = budgetState.maxAmountFormat,
            onValueChange = budgetActions::onAmountChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        CycleFilterField(
            cycles = listOf(Cycle.MONTHLY, Cycle.WEEKLY, Cycle.CUSTOM),
            selectedCycle = budgetState.cycle,
            onCycleChange = budgetActions::onCycleChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.start_date),
            value = DateHelper.formatDateToReadable(budgetState.startDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_budgeting_start_date),
            isEnable = budgetState.cycle == Cycle.CUSTOM,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetState.cycle == Cycle.CUSTOM) {
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
            isEnable = budgetState.cycle == Cycle.CUSTOM,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetState.cycle == Cycle.CUSTOM) {
                            budgetActions.onEndDateClick()
                        }
                    }
                )
                .padding(horizontal = 16.dp)
        )
        BudgetTextSwitchField(
            isOverflowAllowed = budgetState.isOverflowAllowed,
            onOverflowAllowedChange = budgetActions::onOverflowAllowedChange,
            isAutoUpdate = budgetState.isAutoUpdate,
            onAutoUpdateChange = budgetActions::onAutoUpdateChange,
            budgetCycle = budgetState.cycle,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { budgetActions.onAddNewBudget(budgetState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.add),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

data class AddBudgetContentState(
    val category: Int,
    val maxAmount: Long,
    val maxAmountFormat: TextFieldValue,
    val cycle: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)

interface AddBudgetContentActions {
    fun onNavigateToCategory()
    fun onAmountChange(maxAmountFormat: TextFieldValue)
    fun onCycleChange(cycle: Int)
    fun onStartDateClick()
    fun onEndDateClick()
    fun onOverflowAllowedChange(isOverflowAllowed: Boolean)
    fun onAutoUpdateChange(isAutoUpdate: Boolean)
    fun onAddNewBudget(budgetState: AddBudgetContentState)
}