package com.android.monu.presentation.screen.transaction.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.monu.R
import com.android.monu.presentation.components.ActionButton
import com.android.monu.presentation.components.OutlinedActionButton
import com.android.monu.presentation.screen.transaction.TransactionFilterData
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.extensions.toShortMonthResourceId
import kotlin.math.ceil

@Composable
fun TransactionFilterDialog(
    availableTransactionYears: List<Int>,
    transactionFilterData: TransactionFilterData,
    modifier: Modifier = Modifier,
    onApplyClick: (Int?, Int?) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            TransactionFilterMenu(
                availableTransactionYears = availableTransactionYears,
                transactionFilterData = transactionFilterData,
                onDismissRequest = onDismissRequest,
                onApplyClick = onApplyClick
            )
        }
    }
}

@Composable
fun TransactionFilterMenu(
    availableTransactionYears: List<Int>,
    transactionFilterData: TransactionFilterData,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onApplyClick: (Int?, Int?) -> Unit,
) {
    val yearOptionsFilter = listOf(0) + availableTransactionYears
    val monthOptionsFilter = (0..12).toList()

    var tempSelectedYearOption by remember { mutableStateOf(transactionFilterData.selectedYear) }
    var tempSelectedMonthOption by remember { mutableStateOf(transactionFilterData.selectedMonth) }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        RadioGrid(
            title = stringResource(R.string.years),
            options = yearOptionsFilter,
            selectedOption = tempSelectedYearOption ?: 0,
            onOptionSelected = { year ->
                tempSelectedYearOption = if (year == 0) null else year
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        RadioGrid(
            title = stringResource(R.string.months),
            options = monthOptionsFilter,
            selectedOption = tempSelectedMonthOption ?: 0,
            onOptionSelected = { month ->
                tempSelectedMonthOption = if (month == 0) null else month
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            OutlinedActionButton(
                text = stringResource(R.string.cancel),
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .padding(end = 4.dp),
                onClick = onDismissRequest
            )
            ActionButton(
                text = stringResource(R.string.apply),
                color = Blue,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .padding(start = 4.dp),
                onClick = {
                    onApplyClick(tempSelectedYearOption, tempSelectedMonthOption)
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun RadioGrid(
    title: String,
    options: List<Int>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = interFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    )
    HorizontalDivider(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        thickness = 1.5.dp,
        color = SoftGrey
    )

    val rows = ceil(options.size / 3.toDouble()).toInt()
    for (rowIndex in 0 until rows) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (colIndex in 0 until 3) {
                val index = rowIndex * 3 + colIndex
                if (index < options.size) {
                    val option = options[index]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onOptionSelected(option) }
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { onOptionSelected(option) },
                            modifier = Modifier.size(40.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Blue,
                                unselectedColor = Color.Gray
                            )
                        )
                        Text(
                            text = if (option in 0..12)
                                stringResource(option.toShortMonthResourceId()) else option.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color.Black
                            )
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}