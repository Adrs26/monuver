package com.android.monu.presentation.screen.budgeting.editbudget.components

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
import com.android.monu.presentation.screen.budgeting.addbudget.components.TextSwitchField
import com.android.monu.presentation.utils.DatabaseCodeMapper
import com.android.monu.presentation.utils.DateHelper

@Composable
fun EditBudgetingContent(
    budgetState: EditBudgetContentState,
    budgetActions: EditBudgetContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StaticTextInputField(
            title = stringResource(R.string.category),
            value = stringResource(DatabaseCodeMapper.toParentCategoryTitle(budgetState.category)),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.maximum_amount),
            value = budgetState.maxAmountFormat,
            onValueChange = { budgetActions.onAmountChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.start_date),
            value = DateHelper.formatDateToReadable(budgetState.startDate),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_budgeting_start_date),
            isEnable = budgetState.period == 3,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetState.period == 3) {
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
            isEnable = budgetState.period == 3,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (budgetState.period == 3) {
                            budgetActions.onEndDateClick()
                        }
                    }
                )
                .padding(horizontal = 16.dp)
        )
        TextSwitchField(
            isOverflowAllowed = budgetState.isOverflowAllowed,
            isAutoUpdate = budgetState.isAutoUpdate,
            onOverflowAllowedChange = { budgetActions.onOverflowAllowedChange(it) },
            onAutoUpdateChange = { budgetActions.onAutoUpdateChange(it) },
            budgetingPeriod = budgetState.period,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = { budgetActions.onEditBudget(budgetState) },
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

data class EditBudgetContentState(
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

interface EditBudgetContentActions {
    fun onAmountChange(maxAmountFormat: TextFieldValue)
    fun onStartDateClick()
    fun onEndDateClick()
    fun onOverflowAllowedChange(isOverflowAllowed: Boolean)
    fun onAutoUpdateChange(isAutoUpdate: Boolean)
    fun onEditBudget(budgetState: EditBudgetContentState)
}