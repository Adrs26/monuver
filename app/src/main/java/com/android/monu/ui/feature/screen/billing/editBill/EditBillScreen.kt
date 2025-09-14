package com.android.monu.ui.feature.screen.billing.editBill

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
import com.android.monu.ui.feature.screen.billing.editBill.components.EditBillContent
import com.android.monu.ui.feature.screen.billing.editBill.components.EditBillContentActions
import com.android.monu.ui.feature.screen.billing.editBill.components.EditBillContentState
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBillScreen(
    billState: EditBillState,
    onNavigateBack: () -> Unit,
    onEditBill: (EditBillContentState) -> Unit
) {
    var billTitle by rememberSaveable { mutableStateOf(billState.title) }
    var billDate by rememberSaveable { mutableStateOf(billState.date) }
    var billAmount by rememberSaveable { mutableLongStateOf(billState.amount) }
    var billAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberFormatHelper.formatToRupiah(billAmount)))
    }
    var isBillRecurring by rememberSaveable { mutableStateOf(billState.isRecurring) }
    var billCycle by rememberSaveable { mutableIntStateOf(billState.cycle) }
    var billPeriod by rememberSaveable { mutableIntStateOf(billState.period) }
    var billFixPeriod by rememberSaveable { mutableStateOf(billState.fixPeriod) }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val editBillContentState = EditBillContentState(
        id = billState.id,
        title = billTitle,
        date = billDate,
        amount = billAmount,
        amountFormat = billAmountFormat,
        isRecurring = isBillRecurring,
        cycle = billCycle,
        period = billPeriod,
        fixPeriod = billFixPeriod,
        nowPaidPeriod = billState.nowPaidPeriod
    )

    val editBillContentActions = object : EditBillContentActions {
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
        }

        override fun onFixPeriodChange(fixPeriod: String) {
            billFixPeriod = fixPeriod
        }

        override fun onEditBill(billState: EditBillContentState) {
            onEditBill(billState)
        }
    }

    LaunchedEffect(billState.editResult) {
        billState.editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.UpdateBillSuccess) {
                onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_bill),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        EditBillContent(
            billState = editBillContentState,
            billActions = editBillContentActions,
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

data class EditBillState(
    val id: Long,
    val title: String,
    val date: String,
    val amount: Long,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String,
    val nowPaidPeriod: Int,
    val editResult: DatabaseResultMessage?
)