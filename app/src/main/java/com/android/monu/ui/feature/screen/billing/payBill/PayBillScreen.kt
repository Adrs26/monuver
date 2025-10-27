package com.android.monu.ui.feature.screen.billing.payBill

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.monu.R
import com.android.monu.domain.common.DatabaseResultState
import com.android.monu.domain.model.PayBillState
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.billing.payBill.components.PayBillContent
import com.android.monu.ui.feature.screen.billing.payBill.components.PayBillContentActions
import com.android.monu.ui.feature.utils.isPayBillSuccess
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.feature.utils.showToast
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayBillScreen(
    billAmount: Long,
    payBillUiState: PayBillUiState,
    payBillActions: PayBillActions
) {
    var billTitle by rememberSaveable { mutableStateOf("Pembayaran Tagihan") }
    var billDate by rememberSaveable {
        mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
    }
    var billAmount by rememberSaveable { mutableLongStateOf(billAmount) }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val payBillContentState = PayBillState(
        title = billTitle,
        parentCategory = payBillUiState.category.first,
        childCategory = payBillUiState.category.second,
        date = billDate,
        amount = billAmount,
        sourceId = payBillUiState.source.first,
        sourceName = payBillUiState.source.second
    )

    val payBillContentActions = object : PayBillContentActions {
        override fun onTitleChange(title: String) {
            billTitle = title
        }

        override fun onNavigateToCategory() {
            payBillActions.onNavigateToCategory()
        }

        override fun onDateClick() {
            calendarState.show()
        }

        override fun onNavigateToSource() {
            payBillActions.onNavigateToSource()
        }

        override fun onPayBill(billState: PayBillState) {
            payBillActions.onPayBill(billState)
        }
    }

    LaunchedEffect(payBillUiState.payResult) {
        payBillUiState.payResult?.let { result ->
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
        }
    ) { innerPadding ->
        PayBillContent(
            billState = payBillContentState,
            billActions = payBillContentActions,
            modifier = Modifier.padding(innerPadding)
        )
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString(), DateTimeFormatter.ISO_LOCAL_DATE)
            val today = LocalDate.now()
            val isAfterToday = inputDate.isAfter(today)
            if (isAfterToday) {
                context.getString(R.string.cannot_select_future_date).showMessageWithToast(context)
            } else {
                billDate = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

data class PayBillUiState(
    val category: Pair<Int, Int>,
    val source: Pair<Int, String>,
    val payResult: DatabaseResultState?,
)

interface PayBillActions {
    fun onNavigateBack()
    fun onNavigateToCategory()
    fun onNavigateToSource()
    fun onPayBill(billState: PayBillState)
}