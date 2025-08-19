package com.android.monu.presentation.screen.bill.addbill

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.bill.addbill.components.AddBillContent
import com.android.monu.presentation.screen.bill.addbill.components.AddBillContentActions
import com.android.monu.presentation.screen.bill.addbill.components.AddBillContentState
import com.android.monu.presentation.utils.Cycle
import com.android.monu.presentation.utils.NumberFormatHelper
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBillScreen(
    onNavigateBack: () -> Unit
) {
    var billTitle by rememberSaveable { mutableStateOf("") }
    var billDate by rememberSaveable { mutableStateOf("") }
    var billAmount by rememberSaveable { mutableLongStateOf(0L) }
    var billAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(billAmount)))
    }
    var isBillRecurring by rememberSaveable { mutableStateOf(false) }
    var billCycle by rememberSaveable { mutableIntStateOf(Cycle.YEARLY) }
    var billSelectedPeriod by rememberSaveable { mutableIntStateOf(1) }
    var billFixPeriod by rememberSaveable { mutableStateOf("") }

    val calendarState = rememberSheetState()

    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Tambah tagihan",
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        AddBillContent(
            billState = AddBillContentState(
                title = billTitle,
                date = billDate,
                amount = billAmount,
                amountFormat = billAmountFormat,
                isRecurring = isBillRecurring,
                cycle = billCycle,
                selectedPeriod = billSelectedPeriod,
                fixPeriod = billFixPeriod
            ),
            billActions = object : AddBillContentActions {
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

                override fun onSelectedPeriodChange(selectedPeriod: Int) {
                    billSelectedPeriod = selectedPeriod
                    if (selectedPeriod == 1) {
                        billFixPeriod = ""
                    }
                }

                override fun onFixPeriodChange(period: String) {
                    if (period.length <= 2 && period.all { it.isDigit() }) {
                        billFixPeriod = period
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString(), DateTimeFormatter.ISO_LOCAL_DATE)
            val today = LocalDate.now()
            val isBeforeToday = inputDate.isBefore(today)
            if (isBeforeToday) {
                billDate = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}