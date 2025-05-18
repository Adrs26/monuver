package com.android.monu.presentation.screen.transactions.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.presentation.components.ActionButton
import com.android.monu.presentation.components.AmountInputField
import com.android.monu.presentation.components.OutlinedActionButton
import com.android.monu.presentation.components.TextInputField
import com.android.monu.presentation.components.Toolbar
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.LightGrey
import com.android.monu.util.CurrencyFormatHelper

@Composable
fun EditTransactionScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val transactionType by remember { mutableStateOf("Expense") }

    var title by remember { mutableStateOf("") }
    var rawAmountInput by remember { mutableLongStateOf(0L) }
    var amountFieldValue by remember { mutableStateOf(TextFieldValue(rawAmountInput.toString())) }

    val category by remember { mutableStateOf("") }
    val date by remember { mutableStateOf("") }
    val budgeting by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrey)
    ) {
        Toolbar(
            title = "Edit Transaction",
            navigateBack = navigateBack
        )
        TextInputField(
            title = stringResource(R.string.title),
            value = title,
            onValueChange = { title = it },
            placeholderText = stringResource(R.string.transaction_title),
        )
        TextInputField(
            title = stringResource(R.string.category),
            value = category,
            onValueChange = {},
            placeholderText = stringResource(R.string.choose_category),
            isEnable = false
        )
        TextInputField(
            title = stringResource(R.string.date),
            value = date,
            onValueChange = {},
            placeholderText = stringResource(R.string.choose_date),
            isEnable = false
        )
        AmountInputField(
            title = stringResource(R.string.amount),
            value = amountFieldValue,
            onValueChange = {
                if (it.text.isEmpty()) {
                    rawAmountInput = 0
                } else {
                    val cleanInput = it.text.replace(Regex("\\D"), "")
                    rawAmountInput = cleanInput.toLong()
                }

                val formattedText = CurrencyFormatHelper.formatToThousandDivider(rawAmountInput)
                val newCursorPosition = formattedText.length

                amountFieldValue = TextFieldValue(
                    text = formattedText,
                    selection = TextRange(newCursorPosition)
                )
            },
            placeholderText = "0",
        )
        if (transactionType == "Expense") {
            TextInputField(
                title = stringResource(R.string.budgeting),
                value = budgeting,
                onValueChange = {},
                placeholderText = stringResource(R.string.choose_budgeting),
                isEnable = false
            )
        }
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp)
        ) {
            OutlinedActionButton(
                text = "Delete",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onClick = navigateBack
            )
            ActionButton(
                text = stringResource(R.string.save),
                color = Blue,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                onClick = navigateBack
            )
        }
    }
}