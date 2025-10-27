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
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.EditBillState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.billing.editBill.components.EditBillContent
import com.android.monu.ui.feature.screen.billing.editBill.components.EditBillContentActions
import com.android.monu.ui.feature.utils.isUpdateBillSuccess
import com.android.monu.ui.feature.utils.showToast
import com.android.monu.utils.NumberHelper
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBillScreen(
    billUiState: EditBillUiState,
    onNavigateBack: () -> Unit,
    onEditBill: (EditBillState) -> Unit
) {
    var billTitle by rememberSaveable { mutableStateOf(billUiState.title) }
    var billDate by rememberSaveable { mutableStateOf(billUiState.date) }
    var billAmount by rememberSaveable { mutableLongStateOf(billUiState.amount) }
    var billAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberHelper.formatToRupiah(billAmount)))
    }
    var billPeriod by rememberSaveable { mutableIntStateOf(billUiState.period) }
    var billFixPeriod by rememberSaveable { mutableStateOf(billUiState.fixPeriod) }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val editBillContentState = EditBillState(
        id = billUiState.id,
        parentId = billUiState.parentId,
        title = billTitle,
        date = billDate,
        amount = billAmount,
        timeStamp = billUiState.timeStamp,
        isRecurring = billUiState.isRecurring,
        cycle = billUiState.cycle,
        period = billPeriod,
        fixPeriod = billFixPeriod,
        nowPaidPeriod = billUiState.nowPaidPeriod,
        isPaidBefore = billUiState.isPaidBefore
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

            val formattedText = NumberHelper.formatToRupiah(billAmount)
            val newCursorPosition = formattedText.length

            billAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onPeriodChange(period: Int) {
            billPeriod = period
        }

        override fun onFixPeriodChange(fixPeriod: String) {
            billFixPeriod = fixPeriod
        }

        override fun onEditBill(billState: EditBillState) {
            onEditBill(billState)
        }
    }

    LaunchedEffect(billUiState.editResult) {
        billUiState.editResult?.let { result ->
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
        }
    ) { innerPadding ->
        EditBillContent(
            billState = editBillContentState,
            billAmountFormat = billAmountFormat,
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

data class EditBillUiState(
    val id: Long,
    val parentId: Long,
    val title: String,
    val date: String,
    val amount: Long,
    val timeStamp: Long,
    val isRecurring: Boolean,
    val cycle: Int,
    val period: Int,
    val fixPeriod: String,
    val nowPaidPeriod: Int,
    val isPaidBefore: Boolean,
    val editResult: DatabaseResultState?
)