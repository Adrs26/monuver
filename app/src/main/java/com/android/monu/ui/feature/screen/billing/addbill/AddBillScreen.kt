package com.android.monu.ui.feature.screen.billing.addbill

import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.android.monu.R
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.billing.addbill.components.AddBillContent
import com.android.monu.ui.feature.screen.billing.addbill.components.AddBillContentActions
import com.android.monu.ui.feature.screen.billing.addbill.components.AddBillContentState
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillScreen(
    addResult: DatabaseResultMessage?,
    onNavigateBack: () -> Unit,
    onAddNewBill: (AddBillContentState) -> Unit
) {
    var billTitle by rememberSaveable { mutableStateOf("") }
    var billDate by rememberSaveable { mutableStateOf("") }
    var billAmount by rememberSaveable { mutableLongStateOf(0L) }
    var billAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberFormatHelper.formatToRupiah(billAmount)))
    }
    var isBillRecurring by rememberSaveable { mutableStateOf(false) }
    var billCycle by rememberSaveable { mutableIntStateOf(Cycle.YEARLY) }
    var billPeriod by rememberSaveable { mutableIntStateOf(1) }
    var billFixPeriod by rememberSaveable { mutableStateOf("") }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val billState = AddBillContentState(
        title = billTitle,
        date = billDate,
        amount = billAmount,
        amountFormat = billAmountFormat,
        isRecurring = isBillRecurring,
        cycle = billCycle,
        period = billPeriod,
        fixPeriod = billFixPeriod
    )

    val billActions = object : AddBillContentActions {
        override fun onTitleChange(title: String) {
            billTitle = title
        }

        override fun onDateClick() {
            calendarState.show()
        }

        override fun onAmountChange(amountFormat: TextFieldValue) {
            val cleanInput = amountFormat.text.replace(Regex("\\D"), "")
            billAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberFormatHelper.formatToRupiah(billAmount)
            val newCursorPosition = formattedText.length

            billAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onRecurringChange(isRecurring: Boolean) {
            isBillRecurring = isRecurring
        }

        override fun onCycleChange(cycle: Int) {
            billCycle = cycle
        }

        override fun onPeriodChange(period: Int) {
            billPeriod = period
            if (billPeriod == 1) {
                billFixPeriod = ""
            }
        }

        override fun onFixPeriodChange(fixPeriod: String) {
            if (fixPeriod.length <= 2 && fixPeriod.all { it.isDigit() }) {
                billFixPeriod = fixPeriod
            }
        }

        override fun onAddNewBill(billState: AddBillContentState) {
            onAddNewBill(billState)
        }
    }

    LaunchedEffect(addResult) {
        addResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.CreateBillSuccess) {
                onNavigateBack()
            }
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
        AddBillContent(
            billState = billState,
            billActions = billActions,
            modifier = Modifier.padding(innerPadding)
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            billDate = selectedDate.toString()
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}