package com.android.monu.presentation.screen.transactions.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.presentation.components.ActionButton
import com.android.monu.presentation.components.AmountInputField
import com.android.monu.presentation.components.TextInputField
import com.android.monu.presentation.components.Toolbar
import com.android.monu.presentation.screen.transactions.transaction.component.CategoryBottomSheetContent
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.LightGrey
import com.android.monu.util.CurrencyFormatHelper
import com.android.monu.util.DateHelper
import com.android.monu.util.debouncedClickable
import com.android.monu.util.showMessageWithToast
import com.android.monu.util.toCategoryCode
import com.android.monu.util.toCategoryName
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    category: Int,
    date: String,
    insertResult: Result<Long>?,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onResetInsertResultValue: () -> Unit,
    onCategoryChange: (Int) -> Unit,
    onDateChange: (String) -> Unit,
    onSaveButtonClick: (String, Int, Int, String, Long) -> Unit,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var rawAmountInput by rememberSaveable { mutableLongStateOf(0L) }
    var amountFieldValue by remember {
        mutableStateOf(TextFieldValue(
            text = CurrencyFormatHelper.formatToThousandDivider(rawAmountInput))
        )
    }
    var showCategoryBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val calendarState = rememberSheetState()
    val context = LocalContext.current

    LaunchedEffect(insertResult) {
        insertResult?.let {
            if (it.isSuccess) {
                context.getString(R.string.transaction_successfully_saved)
                    .showMessageWithToast(context)
                onResetInsertResultValue()
                navigateBack()
            } else {
                if (it.exceptionOrNull() is IllegalArgumentException) {
                    context.getString(R.string.empty_input_field).showMessageWithToast(context)
                } else {
                    context.getString(R.string.transaction_failed_to_saved)
                        .showMessageWithToast(context)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrey)
    ) {
        Toolbar(
            title = stringResource(R.string.add_income),
            navigateBack = navigateBack
        )
        TextInputField(
            title = stringResource(R.string.title),
            value = title,
            onValueChange = { title = it },
            placeholderText = stringResource(R.string.transaction_title),
        )
        TextInputField(
            title = stringResource(R.string.category),
            value = if (category != 0) stringResource(category.toCategoryName()) else "",
            onValueChange = {},
            placeholderText = stringResource(R.string.choose_category),
            modifier = Modifier.debouncedClickable { showCategoryBottomSheet = true },
            isEnable = false
        )
        TextInputField(
            title = stringResource(R.string.date),
            value = DateHelper.formatDateToReadable(date),
            onValueChange = {},
            placeholderText = stringResource(R.string.choose_date),
            modifier = Modifier.debouncedClickable { calendarState.show() },
            isEnable = false
        )
        AmountInputField(
            title = stringResource(R.string.amount),
            value = amountFieldValue,
            onValueChange = {
                if (it.text.isEmpty()) {
                    rawAmountInput = 0
                } else {
                    val cleanInput = it.text.replace(Regex("\\D"), "")
                    rawAmountInput = cleanInput.toLong()
                }

                val formattedText = CurrencyFormatHelper.formatToThousandDivider(rawAmountInput)
                val newCursorPosition = formattedText.length

                amountFieldValue = TextFieldValue(
                    text = formattedText,
                    selection = TextRange(newCursorPosition)
                )
            },
            placeholderText = "0",
        )
        ActionButton(
            text = stringResource(R.string.save),
            color = Blue,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp),
            onClick = { onSaveButtonClick(title, 1, category, date, rawAmountInput) }
        )
    }

    if (showCategoryBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategoryBottomSheet = false },
            sheetState = sheetState,
            shape = BottomSheetDefaults.ExpandedShape,
            containerColor = Color.White,
            dragHandle = {
                Box(
                    Modifier
                        .padding(vertical = 8.dp)
                        .size(width = 36.dp, height = 2.dp)
                        .background(Color.Gray, RoundedCornerShape(2.dp))
                )
            }
        ) {
            CategoryBottomSheetContent(
                categoryType = "Income",
                onItemClick = { categoryResId ->
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showCategoryBottomSheet = false
                            onCategoryChange(categoryResId.toCategoryCode())
                        }
                    }
                }
            )
        }
    }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { selectedDate ->
            onDateChange(selectedDate.toString())
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        )
    )
}