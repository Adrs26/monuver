package com.android.monuver.feature.billing.presentation.addBill

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.android.monuver.core.domain.util.Cycle
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.CycleFilterField
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.components.TextWithSwitch
import com.android.monuver.core.presentation.util.isCreateBillSuccess
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.feature.billing.R
import com.android.monuver.feature.billing.domain.model.AddBillState
import com.android.monuver.feature.billing.presentation.components.BillPeriodRadioGroupField
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddBillScreen(
    result: DatabaseResultState?,
    onNavigateBack: () -> Unit,
    onAddNewBill: (AddBillState) -> Unit
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableLongStateOf(0L) }
    var formattedAmount by remember { mutableStateOf(TextFieldValue(amount.toRupiah())) }
    var isRecurring by rememberSaveable { mutableStateOf(false) }
    var cycle by rememberSaveable { mutableIntStateOf(Cycle.YEARLY) }
    var period by rememberSaveable { mutableIntStateOf(1) }
    var fixPeriod by rememberSaveable { mutableStateOf("") }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isCreateBillSuccess()) onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.add_bill),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
            TextWithSwitch(
                text = stringResource(R.string.recurring_bill),
                checked = isRecurring,
                onCheckedChange = { isRecurring = it },
                isEnable = true,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            AnimatedVisibility(
                visible = isRecurring,
                enter = slideInVertically(initialOffsetY = { -it / 3 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it / 3 }) + fadeOut()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    CycleFilterField(
                        cycles = listOf(Cycle.YEARLY, Cycle.MONTHLY, Cycle.WEEKLY),
                        selectedCycle = cycle,
                        onCycleChange = { cycle = it },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    BillPeriodRadioGroupField(
                        selectedPeriod = period,
                        onPeriodSelect = { period = it },
                        fixPeriod = fixPeriod,
                        onFixPeriodChange = { fixPeriod = it },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    onAddNewBill(
                        AddBillState(
                            title = title,
                            date = date,
                            amount = amount,
                            isRecurring = isRecurring,
                            cycle = cycle,
                            period = period,
                            fixPeriod = fixPeriod
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.add),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium
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