package com.android.monu.ui.feature.screen.billing.billDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.domain.model.bill.Bill
import com.android.monu.ui.feature.screen.transaction.transactionDetail.components.DataDivider
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.ui.feature.utils.DateHelper
import com.android.monu.ui.feature.utils.NumberFormatHelper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun BillDetailContent(
    bill: Bill,
    onNavigateToPayBill: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BillDetailData(
            title = stringResource(R.string.title),
            content = bill.title
        )
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.due_date),
            content = DateHelper.formatDateToReadable(bill.dueDate)
        )
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.amount),
            content = NumberFormatHelper.formatToRupiah(bill.amount)
        )
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.type),
            content = if (bill.isRecurring) stringResource(R.string.recurring_bill) else
                stringResource(R.string.one_time_bill)
        )
        if (bill.isRecurring) {
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.cycle),
                content = stringResource(DatabaseCodeMapper.toCycle(bill.cycle ?: 0))
            )
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.bill_period),
                content = if (bill.period == 1) stringResource(R.string.unlimited) else
                    stringResource(R.string.fix_period_times, bill.fixPeriod ?: 0)
            )
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.payment_number),
                content = bill.nowPaidPeriod.toString()
            )
        }
        DataDivider()
        BillDetailData(
            title = stringResource(R.string.status),
            content = getStatusInformationText(bill.dueDate, bill.isPaid)
        )
        if (bill.isPaid) {
            DataDivider()
            BillDetailData(
                title = stringResource(R.string.paid_on),
                content = DateHelper.formatDateToReadable(bill.paidDate ?: "2025-08-21")
            )
        }
        if (!bill.isPaid) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onNavigateToPayBill(bill.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = stringResource(R.string.pay_bill),
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