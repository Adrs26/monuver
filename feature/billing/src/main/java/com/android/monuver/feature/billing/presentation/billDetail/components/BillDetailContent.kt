package com.android.monuver.feature.billing.presentation.billDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.feature.billing.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun BillDetailContent(
    billState: BillState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BillDetailData(
            title = stringResource(R.string.title),
            content = billState.title,
            modifier = Modifier.padding(top = 32.dp)
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        BillDetailData(
            title = stringResource(R.string.due_date),
            content = DateHelper.formatToReadable(billState.dueDate)
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        BillDetailData(
            title = stringResource(R.string.amount),
            content = billState.amount.toRupiah()
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        BillDetailData(
            title = stringResource(R.string.type),
            content = if (billState.isRecurring) stringResource(R.string.recurring_bill) else
                stringResource(R.string.one_time_bill)
        )
        if (billState.isRecurring) {
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            BillDetailData(
                title = stringResource(R.string.cycle),
                content = stringResource(DatabaseCodeMapper.toCycle(billState.cycle ?: 0))
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            BillDetailData(
                title = stringResource(R.string.bill_period),
                content = if (billState.period == 1) stringResource(R.string.unlimited) else
                    stringResource(R.string.fix_period_times, billState.fixPeriod ?: 0)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            BillDetailData(
                title = stringResource(R.string.payment_number),
                content = billState.nowPaidPeriod.toString()
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
        BillDetailData(
            title = stringResource(R.string.status),
            content = getStatusInformationText(billState.dueDate, billState.isPaid)
        )
        if (billState.isPaid) {
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            BillDetailData(
                title = stringResource(R.string.paid_on),
                content = DateHelper.formatToReadable(billState.paidDate ?: "2025-08-21"),
                modifier = Modifier.padding(bottom = 32.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun BillDetailData(
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

@OptIn(ExperimentalTime::class)
@Composable
private fun getStatusInformationText(dueDate: String, isPaid: Boolean): String {
    return if (isPaid) {
        stringResource(R.string.paid)
    } else {
        val inputDate = LocalDate.parse(dueDate)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        if (inputDate < today || inputDate == today) {
            stringResource(R.string.overdue)
        } else {
            stringResource(R.string.waiting_for_pay)
        }
    }
}