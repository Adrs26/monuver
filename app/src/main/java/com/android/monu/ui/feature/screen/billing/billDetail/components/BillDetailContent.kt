package com.android.monu.ui.feature.screen.billing.billDetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.BillState
import com.android.monu.ui.feature.screen.transaction.transactionDetail.components.DataDivider
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.utils.DateHelper
import com.android.monu.utils.NumberHelper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun BillDetailContent(
    billState: BillState,
    onNavigateToPayBill: (Long) -> Unit,
    onCancelBillPayment: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BillDetailData(
            title = stringResource(R.string.title),
            content = billState.title
        )
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.due_date),
            content = DateHelper.formatDateToReadable(billState.dueDate)
        )
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.amount),
            content = NumberHelper.formatToRupiah(billState.amount)
        )
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.type),
            content = if (billState.isRecurring) stringResource(R.string.recurring_bill) else
                stringResource(R.string.one_time_bill)
        )
        if (billState.isRecurring) {
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.cycle),
                content = stringResource(DatabaseCodeMapper.toCycle(billState.cycle ?: 0))
            )
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.bill_period),
                content = if (billState.period == 1) stringResource(R.string.unlimited) else
                    stringResource(R.string.fix_period_times, billState.fixPeriod ?: 0)
            )
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.payment_number),
                content = billState.nowPaidPeriod.toString()
            )
        }
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.status),
            content = getStatusInformationText(billState.dueDate, billState.isPaid)
        )
        if (billState.isPaid) {
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.paid_on),
                content = DateHelper.formatDateToReadable(billState.paidDate ?: "2025-08-21")
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!billState.isPaid) {
            Button(
                onClick = { onNavigateToPayBill(billState.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.pay_bill),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        } else {
            OutlinedButton(
                onClick = { onCancelBillPayment(billState.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Text(
                    text = stringResource(R.string.cancel_bill_payment),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun BillDetailData(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        )
        Text(
            text = content,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
        )
    }
}

@Composable
private fun getStatusInformationText(dueDate: String, isPaid: Boolean): String {
    return if (isPaid) {
        stringResource(R.string.paid)
    } else {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val inputDate = LocalDate.parse(dueDate, formatter)
        val today = LocalDate.now()

        if (inputDate.isBefore(today) || inputDate == today) {
            stringResource(R.string.overdue)
        } else {
            stringResource(R.string.waiting_for_pay)
        }
    }
}