package com.android.monu.presentation.screen.budgeting.editbudgeting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.android.monu.presentation.components.StaticTextInputField
import com.android.monu.presentation.components.TextAmountInputField
import com.android.monu.presentation.components.TextDateInputField
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.TextSwitchField
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.DateHelper

@Composable
fun EditBudgetingContent(
    budgetingState: EditBudgetingContentState,
    budgetingActions: EditBudgetingContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StaticTextInputField(
            title = stringResource(R.string.category),
            value = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetingState.category)),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.maximum_amount),
            value = budgetingState.maxAmountFormat,
            onValueChange = { budgetingActions.onAmountChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.start_date),
            value = DateHelper.formatDateToReadable(budgetingState.startDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_budgeting_start_date),
            isEnable = budgetingState.period == 3,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetingState.period == 3) {
                            budgetingActions.onStartDateClick()
                        }
                    }
                )
                .padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.end_date),
            value = DateHelper.formatDateToReadable(budgetingState.endDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_budgeting_end_date),
            isEnable = budgetingState.period == 3,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetingState.period == 3) {
                            budgetingActions.onEndDateClick()
                        }
                    }
                )
                .padding(horizontal = 16.dp)
        )
        TextSwitchField(
            isOverflowAllowed = budgetingState.isOverflowAllowed,
            isAutoUpdate = budgetingState.isAutoUpdate,
            onOverflowAllowedChange = { budgetingActions.onOverflowAllowedChange(it) },
            onAutoUpdateChange = { budgetingActions.onAutoUpdateChange(it) },
            budgetingPeriod = budgetingState.period,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = { budgetingActions.onEditBudgeting(budgetingState) },
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

data class EditBudgetingContentState(
    val id: Long,
    val category: Int,
    val maxAmount: Long,
    val maxAmountFormat: TextFieldValue,
    val period: Int,
    val startDate: String,
    val endDate: String,
    val isOverflowAllowed: Boolean,
    val isAutoUpdate: Boolean
)

interface EditBudgetingContentActions {
    fun onAmountChange(maxAmountFormat: TextFieldValue)
    fun onStartDateClick()
    fun onEndDateClick()
    fun onOverflowAllowedChange(isOverflowAllowed: Boolean)
    fun onAutoUpdateChange(isAutoUpdate: Boolean)
    fun onEditBudgeting(budgetingState: EditBudgetingContentState)
}