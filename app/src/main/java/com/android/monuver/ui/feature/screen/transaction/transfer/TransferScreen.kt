package com.android.monuver.ui.feature.screen.transaction.transfer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.android.monuver.R
import com.android.monuver.domain.common.DatabaseResultState
import com.android.monuver.domain.model.TransferState
import com.android.monuver.ui.feature.components.CommonAppBar
import com.android.monuver.ui.feature.screen.transaction.transfer.components.TransferContent
import com.android.monuver.ui.feature.screen.transaction.transfer.components.TransferContentActions
import com.android.monuver.ui.feature.utils.isCreateTransactionSuccess
import com.android.monuver.ui.feature.utils.showMessageWithToast
import com.android.monuver.ui.feature.utils.showToast
import com.android.monuver.utils.NumberHelper
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    transferUiState: TransferUiState,
    transferActions: TransferActions
) {
    var transferDate by rememberSaveable { mutableStateOf("") }
    var transferAmount by rememberSaveable { mutableLongStateOf(0L) }
    var transferAmountFormat by remember {
        mutableStateOf(TextFieldValue(NumberHelper.formatToRupiah(transferAmount)))
    }

    val calendarState = rememberUseCaseState()
    val context = LocalContext.current

    val transferContentState = TransferState(
        sourceId = transferUiState.source.first,
        sourceName = transferUiState.source.second,
        destinationId = transferUiState.destination.first,
        destinationName = transferUiState.destination.second,
        date = transferDate,
        amount = transferAmount,
    )

    val transferContentActions = object : TransferContentActions {
        override fun onNavigateToSourceAccount() {
            transferActions.onNavigateToSourceAccount()
        }

        override fun onNavigateToDestinationAccount() {
            transferActions.onNavigateToDestinationAccount()
        }

        override fun onDateClick() {
            calendarState.show()
        }

        override fun onAmountChange(amountFormat: TextFieldValue) {
            val cleanInput = amountFormat.text.replace(Regex("\\D"), "")
            transferAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberHelper.formatToRupiah(transferAmount)
            val newCursorPosition = formattedText.length

            transferAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onAddNewTransfer(transferState: TransferState) {
            transferActions.onAddNewTransfer(transferState)
        }
    }

    LaunchedEffect(transferUiState.addResult) {
        transferUiState.addResult?.let { result ->
            result.showToast(context)
            if (result.isCreateTransactionSuccess()) transferActions.onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.transfer_account),
                onNavigateBack = transferActions::onNavigateBack
            )
        }
    ) { innerPadding ->
        TransferContent(
            transferState = transferContentState,
            transferAmountFormat = transferAmountFormat,
            transferActions = transferContentActions,
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
                transferDate = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

data class TransferUiState(
    val source: Pair<Int, String>,
    val destination: Pair<Int, String>,
    val addResult: DatabaseResultState?,
)

interface TransferActions {
    fun onNavigateBack()
    fun onNavigateToSourceAccount()
    fun onNavigateToDestinationAccount()
    fun onAddNewTransfer(transferState: TransferState)
}