package com.android.monu.ui.feature.screen.billing.addBill.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import com.android.monu.ui.feature.components.TextWithSwitch
import com.android.monu.ui.feature.screen.billing.components.BillPeriodRadioGroupField
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.DateHelper

@Composable
fun AddBillContent(
    billState: AddBillContentState,
    billActions: AddBillContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = stringResource(R.string.title),
            value = billState.title,
            onValueChange = billActions::onTitleChange,
            placeholderText = "Masukkan judul tagihan",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.due_date),
            value = DateHelper.formatDateToReadable(billState.date),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_due_date),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = billActions::onDateClick
                )
                .padding(horizontal = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.amount),
            value = billState.amountFormat,
            onValueChange = billActions::onAmountChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextWithSwitch(
            text = stringResource(R.string.recurring_bill),
            checked = billState.isRecurring,
            onCheckedChange = billActions::onRecurringChange,
            isEnable = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        AnimatedVisibility(
            visible = billState.isRecurring,
            enter = slideInVertically(initialOffsetY = { -it / 3 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it / 3 }) + fadeOut()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CycleFilterField(
                    cycles = listOf(Cycle.YEARLY, Cycle.MONTHLY, Cycle.WEEKLY),
                    selectedCycle = billState.cycle,
                    onCycleChange = billActions::onCycleChange,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                BillPeriodRadioGroupField(
                    selectedPeriod = billState.period,
                    onPeriodSelect = billActions::onPeriodChange,
                    fixPeriod = billState.fixPeriod,
                    onFixPeriodChange = billActions::onFixPeriodChange,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { billActions.onAddNewBill(billState) },
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

data class AddBillContentState(
    val title: String,
    val date: String,
    val amount: Long,
    val amountFormat: TextFieldValue,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String
)

interface AddBillContentActions {
    fun onTitleChange(title: String)
    fun onDateClick()
    fun onAmountChange(amountFormat: TextFieldValue)
    fun onRecurringChange(isRecurring: Boolean)
    fun onCycleChange(cycle: Int)
    fun onPeriodChange(period: Int)
    fun onFixPeriodChange(fixPeriod: String)
    fun onAddNewBill(billState: AddBillContentState)
}