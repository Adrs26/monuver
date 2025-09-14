package com.android.monu.ui.feature.screen.billing.payBill.components

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
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.ui.feature.components.StaticTextInputField
import com.android.monu.ui.feature.components.TextDateInputField
import com.android.monu.ui.feature.components.TextInputField
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.NumberFormatHelper

@Composable
fun PayBillContent(
    billState: PayBillContentState,
    billActions: PayBillContentActions,
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
            placeholderText = stringResource(R.string.enter_transaction_title),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextInputField(
            title = stringResource(R.string.category),
            value = if (billState.childCategory == 0) "" else
                stringResource(DatabaseCodeMapper.toChildCategoryTitle(billState.childCategory)),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_transaction_category),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = billActions::onNavigateToCategory
                )
                .padding(horizontal = 16.dp),
            isEnable = false
        )
        TextDateInputField(
            title = stringResource(R.string.date),
            value = DateHelper.formatDateToReadable(billState.date),
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_transaction_date),
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { billActions.onDateClick() }
                )
                .padding(horizontal = 16.dp)
        )
        StaticTextInputField(
            title = stringResource(R.string.amount),
            value = NumberFormatHelper.formatToRupiah(billState.amount),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextInputField(
            title = stringResource(R.string.funds_source),
            value = billState.sourceName,
            onValueChange = { },
            placeholderText = stringResource(R.string.choose_funds_source),
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { billActions.onNavigateToSource() }
                )
                .padding(horizontal = 16.dp),
            isEnable = false
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { billActions.onPayBill(billState) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.pay),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

data class PayBillContentState(
    val title: String,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String
)

interface PayBillContentActions {
    fun onTitleChange(title: String)
    fun onNavigateToCategory()
    fun onDateClick()
    fun onNavigateToSource()
    fun onPayBill(billState: PayBillContentState)
}