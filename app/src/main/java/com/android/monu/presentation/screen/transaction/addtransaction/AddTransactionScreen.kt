package com.android.monu.presentation.screen.transaction.addtransaction

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
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContent
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentActions
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentState
import com.android.monu.presentation.utils.DatabaseResultMessage
import com.android.monu.presentation.utils.NumberFormatHelper
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.presentation.utils.mapResultMessageToStringResource
import com.android.monu.presentation.utils.showMessageWithToast
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    transactionState: AddTransactionState,
    transactionActions: AddTransactionActions
) {
    var transactionTitle by rememberSaveable { mutableStateOf("") }
    var transactionDate by rememberSaveable { mutableStateOf("") }
    var transactionAmount by rememberSaveable { mutableLongStateOf(0L) }
    var transactionAmountFormat by remember {
        mutableStateOf(TextFieldValue(text = NumberFormatHelper.formatToRupiah(transactionAmount)))
    }

    val calendarState = rememberSheetState()
    val context = LocalContext.current

    val addTransactionContentState = AddTransactionContentState(
        title = transactionTitle,
        type = transactionState.type,
        parentCategory = transactionState.category.first,
        childCategory = transactionState.category.second,
        date = transactionDate,
        amount = transactionAmount,
        amountFormat = transactionAmountFormat,
        sourceId = transactionState.source.first,
        sourceName = transactionState.source.second
    )

    val addTransactionContentActions = object : AddTransactionContentActions {
        override fun onTitleChange(title: String) {
            transactionTitle = title
        }

        override fun onNavigateToCategory(transactionType: Int) {
            transactionActions.onNavigateToCategory(transactionType)
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

        override fun onNavigateToSource() {
            transactionActions.onNavigateToSource()
        }

        override fun onAddNewTransaction(transactionState: AddTransactionContentState) {
            transactionActions.onAddNewTransaction(transactionState)
        }
    }

    LaunchedEffect(transactionState.addResult) {
        transactionState.addResult?.let { result ->
            context.getString(mapResultMessageToStringResource(result)).showMessageWithToast(context)
            if (result == DatabaseResultMessage.CreateTransactionSuccess) {
                transactionActions.onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (transactionState.type == TransactionType.INCOME)
                    stringResource(R.string.add_income) else
                        stringResource(R.string.add_expense),
                onNavigateBack = { transactionActions.onNavigateBack() }
            )
        }
    ) { innerPadding ->
        AddTransactionContent(
            transactionState = addTransactionContentState,
            transactionActions = addTransactionContentActions,
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

data class AddTransactionState(
    val type: Int,
    val category: Pair<Int, Int>,
    val source: Pair<Int, String>,
    val addResult: DatabaseResultMessage?,
)

interface AddTransactionActions {
    fun onNavigateBack()
    fun onNavigateToCategory(transactionType: Int)
    fun onNavigateToSource()
    fun onAddNewTransaction(transactionState: AddTransactionContentState)
}