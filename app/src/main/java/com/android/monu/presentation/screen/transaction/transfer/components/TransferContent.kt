package com.android.monu.presentation.screen.transaction.transfer.components

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
import com.android.monu.presentation.components.TextAmountInputField
import com.android.monu.presentation.components.TextInputField
import com.android.monu.utils.DateHelper

@Composable
fun TransferContent(
    transferState: TransferContentState,
    transferActions: TransferContentActions,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = "Akun sumber",
            value = transferState.sourceName,
            onValueChange = { },
            placeholderText = "Pilih akun sumber",
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { transferActions.onNavigateToSourceAccount() }
                )
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            isEnable = false
        )
        TextInputField(
            title = "Akun tujuan",
            value = transferState.destinationName,
            onValueChange = { },
            placeholderText = "Pilih akun tujuan",
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { transferActions.onNavigateToDestinationAccount() }
                )
                .padding(horizontal = 16.dp),
            isEnable = false
        )
        TextInputField(
            title = "Tanggal",
            value = DateHelper.formatDateToReadable(transferState.date),
            onValueChange = { },
            placeholderText = "Pilih tanggal transfer",
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { transferActions.onDateClick() }
                )
                .padding(horizontal = 16.dp),
            isEnable = false,
            isDatePicker = true
        )
        TextAmountInputField(
            title = "Nominal",
            value = transferState.amountFormat,
            onValueChange = { transferActions.onAmountChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = {
                transferActions.onAddNewTransfer(transferState)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.transfer),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

data class TransferContentState(
    val type: Int,
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int,
    val destinationName: String,
    val date: String,
    val amount: Long,
    val amountFormat: TextFieldValue
)

interface TransferContentActions {
    fun onNavigateToSourceAccount()
    fun onNavigateToDestinationAccount()
    fun onDateClick()
    fun onAmountChange(amountFormat: TextFieldValue)
    fun onAddNewTransfer(transferState: TransferContentState)
}