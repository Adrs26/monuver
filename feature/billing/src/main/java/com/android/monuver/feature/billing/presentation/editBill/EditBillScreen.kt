package com.android.monuver.feature.billing.presentation.editBill

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.model.BillState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.isUpdateBillSuccess
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.feature.billing.R
import com.android.monuver.feature.billing.domain.model.EditBillState
import com.android.monuver.feature.billing.presentation.components.BillPeriodRadioGroupField
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditBillScreen(
    billState: BillState,
    result: DatabaseResultState?,
    onNavigateBack: () -> Unit,
    onEditBill: (EditBillState) -> Unit
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf(billState.title) }
    var date by rememberSaveable { mutableStateOf(billState.dueDate) }
    var amount by rememberSaveable { mutableLongStateOf(billState.amount) }
    var formattedAmount by remember { mutableStateOf(TextFieldValue(amount.toRupiah())) }
    var period by rememberSaveable { mutableIntStateOf(billState.period ?: 0) }
    var fixPeriod by rememberSaveable { mutableStateOf(billState.fixPeriod?.toString() ?: "") }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isUpdateBillSuccess()) onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_bill),
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            PrimaryActionButton(
                text = stringResource(R.string.save),
                onClick = {
                    onEditBill(
                        EditBillState(
                            id = billState.id,
                            parentId = billState.parentId,
                            title = title,
                            date = date,
                            amount = amount,
                            timeStamp = billState.timeStamp,
                            isRecurring = billState.isRecurring,
                            cycle = billState.cycle ?: 0,
                            period = period,
                            fixPeriod = fixPeriod,
                            nowPaidPeriod = billState.nowPaidPeriod,
                            isPaidBefore = billState.isPaidBefore
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
                placeholderText = stringResource(R.string.enter_bill_title),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextDateInputField(
                title = stringResource(R.string.due_date),
                value = DateHelper.formatToReadable(date),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_due_date),
                isEnable = true,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = calendarState::show
                    )
                    .padding(horizontal = 16.dp)
            )
            TextAmountInputField(
                title = stringResource(R.string.amount),
                value = formattedAmount,
                onValueChange = {
                    amount = it.toRupiahFieldValue().first
                    formattedAmount = it.toRupiahFieldValue().second
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            if (billState.isRecurring && !billState.isPaidBefore) {
                BillPeriodRadioGroupField(
                    selectedPeriod = period,
                    onPeriodSelect = {
                        period = it
                        if (period == 1) fixPeriod = ""
                    },
                    fixPeriod = fixPeriod,
                    onFixPeriodChange = { fixPeriod = it },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            date = selectedDate.toString()
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}