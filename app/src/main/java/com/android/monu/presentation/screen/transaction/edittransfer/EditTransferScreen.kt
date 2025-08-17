package com.android.monu.presentation.screen.transaction.edittransfer

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
import com.android.monu.R
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.transaction.edittransfer.components.EditTransferContent
import com.android.monu.presentation.screen.transaction.edittransfer.components.EditTransferContentActions
import com.android.monu.presentation.screen.transaction.edittransfer.components.EditTransferContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.NumberFormatHelper
import com.android.monu.presentation.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransferScreen(
    transferState: EditTransferState,
    transferActions: EditTransferActions
) {
    var transactionDate by rememberSaveable { mutableStateOf(transferState.date) }
    var transactionAmount by rememberSaveable { mutableLongStateOf(transferState.amount) }
    var transactionAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(transactionAmount)))
    }

    val calendarState = rememberSheetState()
    val context = LocalContext.current

    val editTransferContentState = EditTransferContentState(
        id = transferState.id,
        sourceId = transferState.sourceId,
        sourceName = transferState.sourceName,
        destinationId = transferState.destinationId,
        destinationName = transferState.destinationName,
        date = transactionDate,
        amount = transactionAmount,
        amountFormat = transactionAmountFormat,
        initialAmount = transferState.amount
    )

    val editTransferActions = object : EditTransferContentActions {
        override fun onDateClick() {
            calendarState.show()
        }

        override fun onAmountChange(amountFormat: TextFieldValue) {
            val cleanInput = amountFormat.text.replace(Regex("\\D"), "")
            transactionAmount = try {
                cleanInput.toLong()
            } catch (_: NumberFormatException) { 0L }

            val formattedText = NumberFormatHelper.formatToRupiah(transactionAmount)
            val newCursorPosition = formattedText.length

            transactionAmountFormat = TextFieldValue(
                text = formattedText,
                selection = TextRange(newCursorPosition)
            )
        }

        override fun onEditTransfer(transferState: EditTransferContentState) {
            transferActions.onEditTransfer(transferState)
        }
    }

    LaunchedEffect(transferState.editResult) {
        transferState.editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.UpdateTransactionSuccess) {
                transferActions.onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_transfer),
                onNavigateBack = { transferActions.onNavigateBack() }
            )
        }
    ) { innerPadding ->
        EditTransferContent(
            transferState = editTransferContentState,
            transferActions = editTransferActions,
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
                transactionDate = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

data class EditTransferState(
    val id: Long,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int,
    val destinationName: String,
    val editResult: DatabaseResultMessage?
)

interface EditTransferActions {
    fun onNavigateBack()
    fun onEditTransfer(transferState: EditTransferContentState)
}