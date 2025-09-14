package com.android.monu.ui.feature.screen.saving.withdraw.components

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
import com.android.monu.ui.feature.components.StaticTextInputField
import com.android.monu.ui.feature.components.TextAmountInputField
import com.android.monu.ui.feature.components.TextDateInputField
import com.android.monu.ui.feature.components.TextInputField
import com.android.monu.ui.feature.utils.DateHelper

@Composable
fun WithdrawContent(
    withdrawState: WithdrawContentState,
    withdrawActions: WithdrawContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StaticTextInputField(
            title = stringResource(R.string.saving),
            value = withdrawState.savingName,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.date),
            value = DateHelper.formatDateToReadable(withdrawState.date),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_transaction_date),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = withdrawActions::onDateClick
                )
                .padding(horizontal = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.amount),
            value = withdrawState.amountFormat,
            onValueChange = withdrawActions::onAmountChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextInputField(
            title = stringResource(R.string.destination_account),
            value = withdrawState.accountName,
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_destination_account),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = withdrawActions::onNavigateToAccount
                )
                .padding(horizontal = 16.dp),
            isEnable = false
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { withdrawActions.onAddNewWithdraw(withdrawState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.withdraw),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

data class WithdrawContentState(
    val date: String,
    val amount: Long,
    val amountFormat: TextFieldValue,
    val accountId: Int,
    val accountName: String,
    val savingId: Long,
    val savingName: String
)

interface WithdrawContentActions {
    fun onDateClick()
    fun onAmountChange(amountFormat: TextFieldValue)
    fun onNavigateToAccount()
    fun onAddNewWithdraw(withdrawState: WithdrawContentState)
}