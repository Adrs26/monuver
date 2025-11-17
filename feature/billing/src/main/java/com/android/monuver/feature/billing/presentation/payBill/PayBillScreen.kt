package com.android.monuver.feature.billing.presentation.payBill

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.StaticTextInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.isPayBillSuccess
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.feature.billing.R
import com.android.monuver.feature.billing.domain.model.PayBillState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal fun PayBillScreen(
    category: Pair<Int, Int>,
    billAmount: Long,
    source: Pair<Int, String>,
    result: DatabaseResultState?,
    payBillActions: PayBillActions
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf("Pembayaran Tagihan") }
    var date by rememberSaveable {
        mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString())
    }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isPayBillSuccess()) payBillActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.pay_bill),
                onNavigateBack = payBillActions::onNavigateBack
            )
        },
        bottomBar = {
            PrimaryActionButton(
                text = stringResource(R.string.pay),
                onClick = {
                    payBillActions.onPayBill(
                        PayBillState(
                            title = title,
                            parentCategory = category.first,
                            childCategory = category.second,
                            date = date,
                            amount = billAmount,
                            sourceId = source.first,
                            sourceName = source.second
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextInputField(
                title = stringResource(R.string.title),
                value = title,
                onValueChange = { title = it },
                placeholderText = stringResource(R.string.enter_transaction_title),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextInputField(
                title = stringResource(R.string.category),
                value = if (category.second == 0) "" else
                    stringResource(DatabaseCodeMapper.toChildCategoryTitle(category.second)),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_transaction_category),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = payBillActions::onNavigateToCategory
                    )
                    .padding(horizontal = 16.dp),
                isEnable = false
            )
            TextDateInputField(
                title = stringResource(R.string.date),
                value = DateHelper.formatToReadable(date),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_transaction_date),
                isEnable = true,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = calendarState::show
                    )
                    .padding(horizontal = 16.dp)
            )
            StaticTextInputField(
                title = stringResource(R.string.amount),
                value = billAmount.toRupiah(),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextInputField(
                title = stringResource(R.string.source_account),
                value = source.second,
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_source_account),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = payBillActions::onNavigateToSource
                    )
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isEnable = false
            )
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString())
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            if (inputDate > today) {
                context.getString(R.string.cannot_select_future_date).showMessageWithToast(context)
            } else {
                date = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

internal interface PayBillActions {
    fun onNavigateBack()
    fun onNavigateToCategory()
    fun onNavigateToSource()
    fun onPayBill(billState: PayBillState)
}