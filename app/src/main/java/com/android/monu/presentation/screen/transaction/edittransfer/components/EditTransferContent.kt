package com.android.monu.presentation.screen.transaction.edittransfer.components

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
import com.android.monu.presentation.utils.DateHelper

@Composable
fun EditTransferContent(
    transferState: EditTransferContentState,
    transferActions: EditTransferContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StaticTextInputField(
            title = stringResource(R.string.source_account),
            value = transferState.sourceName,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)

        )
        StaticTextInputField(
            title = stringResource(R.string.destination_account),
            value = transferState.destinationName,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextDateInputField(
            title = stringResource(R.string.date),
            value = DateHelper.formatDateToReadable(transferState.date),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_transfer_date),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { transferActions.onDateClick() }
                )
                .padding(horizontal = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.amount),
            value = transferState.amountFormat,
            onValueChange = { transferActions.onAmountChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = { transferActions.onEditTransfer(transferState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.save),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

data class EditTransferContentState(
    val id: Long,
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int,
    val destinationName: String,
    val date: String,
    val startAmount: Long,
    val amount: Long,
    val amountFormat: TextFieldValue
)

interface EditTransferContentActions {
    fun onDateClick()
    fun onAmountChange(amountFormat: TextFieldValue)
    fun onEditTransfer(transferState: EditTransferContentState)
}