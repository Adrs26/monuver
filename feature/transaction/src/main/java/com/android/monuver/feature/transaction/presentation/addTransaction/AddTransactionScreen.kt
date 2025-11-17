package com.android.monuver.feature.transaction.presentation.addTransaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.PrimaryActionButton
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.feature.transaction.R
import com.android.monuver.feature.transaction.domain.model.AddTransactionState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal fun AddTransactionScreen(
    transactionType: Int,
    transactionCategory: Pair<Int, Int>,
    transactionSource: Pair<Int, String>,
    result: DatabaseResultState?,
    transactionActions: AddTransactionActions
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableLongStateOf(0L) }
    var formattedAmount by remember { mutableStateOf(TextFieldValue(amount.toRupiah())) }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)

            when (result) {
                is DatabaseResultState.CreateSuccessWithWarningCondition -> {
                    transactionActions.onShowWarningAlert(
                        warning = result.warningCondition,
                        category = result.category
                    )
                    transactionActions.onNavigateBack()
                }
                is DatabaseResultState.CreateTransactionSuccess -> {
                    transactionActions.onNavigateBack()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = if (transactionType == TransactionType.INCOME)
                    stringResource(R.string.add_income) else
                        stringResource(R.string.add_expense),
                onNavigateBack = transactionActions::onNavigateBack
            )
        },
        bottomBar = {
            PrimaryActionButton(
                text = stringResource(R.string.add),
                onClick = {
                    transactionActions.onAddNewTransaction(
                        AddTransactionState(
                            title = title,
                            type = transactionType,
                            parentCategory = transactionCategory.first,
                            childCategory = transactionCategory.second,
                            date = date,
                            amount = amount,
                            sourceId = transactionSource.first,
                            sourceName = transactionSource.second
                        )
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TextInputField(
                title = stringResource(R.string.title),
                value = title,
                onValueChange = { title = it },
                placeholderText = stringResource(R.string.enter_transaction_title),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            TextInputField(
                title = stringResource(R.string.category),
                value = if (transactionCategory.second == 0) "" else
                    stringResource(DatabaseCodeMapper.toChildCategoryTitle(transactionCategory.second)),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_transaction_category),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { transactionActions.onNavigateToCategory(transactionType) }
                    )
                    .padding(horizontal = 16.dp),
                isEnable = false
            )
            TextDateInputField(
                title = stringResource(R.string.date),
                value = DateHelper.formatToReadable(date),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_transaction_date),
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
            TextInputField(
                title = if (transactionType == TransactionType.INCOME)
                    stringResource(R.string.destination_account) else
                        stringResource(R.string.source_account),
                value = transactionSource.second,
                onValueChange = { },
                placeholderText = if (transactionType == TransactionType.INCOME)
                    stringResource(R.string.choose_destination_account) else
                        stringResource(R.string.choose_source_account),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = transactionActions::onNavigateToSource
                    )
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                isEnable = false
            )
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            val inputDate = LocalDate.parse(selectedDate.toString())
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            if (inputDate > today) {
                context.getString(R.string.cannot_select_future_date).showMessageWithToast(context)
            } else {
                date = selectedDate.toString()
            }
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        )
    )
}

internal interface AddTransactionActions {
    fun onNavigateBack()
    fun onNavigateToCategory(transactionType: Int)
    fun onNavigateToSource()
    fun onShowWarningAlert(warning: Int, category: Int)
    fun onAddNewTransaction(transactionState: AddTransactionState)
}