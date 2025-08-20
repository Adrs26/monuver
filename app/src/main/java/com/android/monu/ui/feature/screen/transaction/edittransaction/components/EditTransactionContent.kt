package com.android.monu.ui.feature.screen.transaction.edittransaction.components

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
import com.android.monu.ui.feature.components.StaticTextInputField
import com.android.monu.ui.feature.components.TextAmountInputField
import com.android.monu.ui.feature.components.TextDateInputField
import com.android.monu.ui.feature.components.TextInputField
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.ui.feature.utils.DateHelper

@Composable
fun EditTransactionContent(
    transactionState: EditTransactionContentState,
    transactionActions: EditTransactionContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = stringResource(R.string.title),
            value = transactionState.title,
            onValueChange = transactionActions::onTitleChange,
            placeholderText = stringResource(R.string.enter_transaction_title),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextInputField(
            title = stringResource(R.string.category),
            value = if (transactionState.childCategory == 0) "" else
                stringResource(DatabaseCodeMapper.toChildCategoryTitle(transactionState.childCategory)),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_transaction_category),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { transactionActions.onNavigateToCategory(transactionState.type) }
                )
                .padding(horizontal = 16.dp),
            isEnable = false
        )
        TextDateInputField(
            title = stringResource(R.string.date),
            value = DateHelper.formatDateToReadable(transactionState.date),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_transaction_date),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { transactionActions.onDateClick() }
                )
                .padding(horizontal = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.amount),
            value = transactionState.amountFormat,
            onValueChange = transactionActions::onAmountChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        StaticTextInputField(
            title = stringResource(R.string.funds_source),
            value = transactionState.sourceName,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Button(
            onClick = { transactionActions.onEditTransaction(transactionState) },
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

data class EditTransactionContentState(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val initialParentCategory: Int,
    val date: String,
    val initialDate: String,
    val amount: Long,
    val amountFormat: TextFieldValue,
    val initialAmount: Long,
    val sourceId: Int,
    val sourceName: String
)

interface EditTransactionContentActions {
    fun onTitleChange(title: String)
    fun onNavigateToCategory(transactionType: Int)
    fun onDateClick()
    fun onAmountChange(amountFormat: TextFieldValue)
    fun onEditTransaction(transactionState: EditTransactionContentState)
}