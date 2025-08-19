package com.android.monu.presentation.screen.transaction.edittransaction

import androidx.activity.compose.BackHandler
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
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.components.CommonAppBar
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContent
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentActions
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.NumberFormatHelper
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.presentation.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    transactionState: EditTransactionState,
    transactionActions: EditTransactionActions,
    initialTransaction: Transaction
) {
    var transactionTitle by rememberSaveable { mutableStateOf(transactionState.title) }
    var transactionDate by rememberSaveable { mutableStateOf(transactionState.date) }
    var transactionAmount by rememberSaveable { mutableLongStateOf(transactionState.amount) }
    var transactionAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(transactionAmount)))
    }

    val calendarState = rememberSheetState()
    val context = LocalContext.current

    val editTransactionContentState = EditTransactionContentState(
        id = transactionState.id,
        title = transactionTitle,
        type = transactionState.type,
        parentCategory = transactionState.parentCategory,
        childCategory = transactionState.childCategory,
        initialParentCategory = initialTransaction.parentCategory,
        date = transactionDate,
        initialDate = initialTransaction.date,
        amount = transactionAmount,
        amountFormat = transactionAmountFormat,
        initialAmount = initialTransaction.amount,
        sourceId = transactionState.sourceId,
        sourceName = transactionState.sourceName
    )

    val editTransactionContentActions = object : EditTransactionContentActions {
        override fun onTitleChange(title: String) {
            transactionTitle = title
        }

        override fun onNavigateToCategory(transactionType: Int) {
            if (transactionType == TransactionType.INCOME ||
                transactionType == TransactionType.EXPENSE) {
                transactionActions.onNavigateToCategory(transactionType)
            }
        }

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

        override fun onEditTransaction(transactionState: EditTransactionContentState) {
            transactionActions.onEditTransaction(transactionState)
        }
    }

    LaunchedEffect(transactionState.editResult) {
        transactionState.editResult?.let { result ->
            context.getString(result.message).showMessageWithToast(context)
            if (result == DatabaseResultMessage.UpdateTransactionSuccess) {
                transactionActions.onNavigateBack()
            }
        }
    }

    BackHandler { transactionActions.onNavigateBack() }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_transaction),
                onNavigateBack = { transactionActions.onNavigateBack() }
            )
        }
    ) { innerPadding ->
        EditTransactionContent(
            transactionState = editTransactionContentState,
            transactionActions = editTransactionContentActions,
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

data class EditTransactionState(
    val id: Long,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String,
    val editResult: DatabaseResultMessage?
)

interface EditTransactionActions {
    fun onNavigateBack()
    fun onNavigateToCategory(transactionType: Int)
    fun onEditTransaction(transactionState: EditTransactionContentState)
}