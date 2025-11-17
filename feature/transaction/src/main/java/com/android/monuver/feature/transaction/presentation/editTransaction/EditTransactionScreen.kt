package com.android.monuver.feature.transaction.presentation.editTransaction

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.presentation.components.CommonAppBar
import com.android.monuver.core.presentation.components.StaticTextInputField
import com.android.monuver.core.presentation.components.TextAmountInputField
import com.android.monuver.core.presentation.components.TextDateInputField
import com.android.monuver.core.presentation.components.TextInputField
import com.android.monuver.core.presentation.util.DatabaseCodeMapper
import com.android.monuver.core.presentation.util.isUpdateTransactionSuccess
import com.android.monuver.core.presentation.util.showMessageWithToast
import com.android.monuver.core.presentation.util.showToast
import com.android.monuver.core.presentation.util.toRupiahFieldValue
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.feature.transaction.R
import com.android.monuver.feature.transaction.domain.model.EditTransactionState
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
internal fun EditTransactionScreen(
    initialTransactionState: TransactionState,
    transactionState: TransactionState,
    result: DatabaseResultState?,
    transactionActions: EditTransactionActions
) {
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf(transactionState.title) }
    var date by rememberSaveable { mutableStateOf(transactionState.date) }
    var amount by rememberSaveable { mutableLongStateOf(transactionState.amount) }
    var formattedAmount by remember { mutableStateOf(TextFieldValue(amount.toRupiah())) }

    val calendarState = rememberUseCaseState()

    LaunchedEffect(result) {
        result?.let { result ->
            result.showToast(context)
            if (result.isUpdateTransactionSuccess()) transactionActions.onNavigateBack()
        }
    }

    BackHandler { transactionActions.onNavigateBack() }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.edit_transaction),
                onNavigateBack = transactionActions::onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                value = if (transactionState.childCategory == 0) "" else
                    stringResource(DatabaseCodeMapper.toChildCategoryTitle(transactionState.childCategory)),
                onValueChange = { },
                placeholderText = stringResource(R.string.choose_transaction_category),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { transactionActions.onNavigateToCategory(transactionState.type) }
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
                        onClick = { calendarState.show() }
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
            StaticTextInputField(
                title = if (transactionState.type == TransactionType.INCOME)
                    stringResource(R.string.destination_account) else
                        stringResource(R.string.source_account),
                value = transactionState.sourceName,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    transactionActions.onEditTransaction(
                        EditTransactionState(
                            id = transactionState.id,
                            title = title,
                            type = transactionState.type,
                            parentCategory = transactionState.parentCategory,
                            childCategory = transactionState.childCategory,
                            initialParentCategory = initialTransactionState.parentCategory,
                            date = date,
                            initialDate = initialTransactionState.date,
                            amount = amount,
                            initialAmount = initialTransactionState.amount,
                            sourceId = transactionState.sourceId,
                            sourceName = transactionState.sourceName,
                            isLocked = transactionState.isLocked
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.save),
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
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

internal interface EditTransactionActions {
    fun onNavigateBack()
    fun onNavigateToCategory(transactionType: Int)
    fun onEditTransaction(transactionState: EditTransactionState)
}