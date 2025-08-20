package com.android.monu.ui.feature.screen.bill.addbill.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.feature.components.CycleFilterField
import com.android.monu.ui.feature.components.TextAmountInputField
import com.android.monu.ui.feature.components.TextDateInputField
import com.android.monu.ui.feature.components.TextInputField
import com.android.monu.ui.feature.components.TextWithSwitch
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.DateHelper

@Composable
fun AddBillContent(
    billState: AddBillContentState,
    billActions: AddBillContentActions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TextInputField(
            title = stringResource(R.string.title),
            value = billState.title,
            onValueChange = { billActions.onTitleChange(it) },
            placeholderText = "Masukkan judul tagihan",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        TextDateInputField(
            title = "Tanggal jatuh tempo",
            value = DateHelper.formatDateToReadable(billState.date),
            onValueChange = { },
            placeholderText = "Pilih tanggal jatuh tempo",
            isEnable = true,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { billActions.onDateClick() }
                )
                .padding(horizontal = 16.dp)
        )
        TextAmountInputField(
            title = stringResource(R.string.amount),
            value = billState.amountFormat,
            onValueChange = { billActions.onAmountChange(it) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextWithSwitch(
            text = "Tagihan berulang",
            checked = billState.isRecurring,
            onCheckedChange = { billActions.onRecurringChange(it) },
            isEnable = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        AnimatedVisibility(
            visible = billState.isRecurring,
            enter = slideInVertically(initialOffsetY = { -it / 3 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it / 3 }) + fadeOut()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CycleFilterField(
                    cycles = listOf(Cycle.YEARLY, Cycle.MONTHLY, Cycle.WEEKLY),
                    selectedCycle = billState.cycle,
                    onCycleChange = { billActions.onCycleChange(it)  },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                BillPeriodRadioGroupField(
                    selectedPeriod = billState.selectedPeriod,
                    onPeriodSelect = { billActions.onSelectedPeriodChange(it) },
                    fixPeriod = billState.fixPeriod,
                    onFixPeriodChange = { billActions.onFixPeriodChange(it) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        Button(
            onClick = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.add),
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun BillPeriodRadioGroupField(
    selectedPeriod: Int,
    onPeriodSelect: (Int) -> Unit,
    fixPeriod: String,
    onFixPeriodChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Periode pembayaran",
            modifier = Modifier.padding(bottom = 12.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedPeriod == 1,
                    onClick = { onPeriodSelect(1) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "Tanpa batas",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedPeriod == 2,
                    onClick = { onPeriodSelect(2) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                BasicTextField(
                    value = fixPeriod,
                    onValueChange = onFixPeriodChange,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    enabled = selectedPeriod == 2,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .width(44.dp)
                                .height(36.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .background(
                                    if (selectedPeriod == 1)
                                        MaterialTheme.colorScheme.surfaceVariant else
                                            MaterialTheme.colorScheme.background
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selectedPeriod == 1)
                                        MaterialTheme.colorScheme.surfaceVariant else
                                            MaterialTheme.colorScheme.onSurfaceVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                    }
                )
                Text(
                    text = "kali",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}

data class AddBillContentState(
    val title: String,
    val date: String,
    val amount: Long,
    val amountFormat: TextFieldValue,
    val isRecurring: Boolean,
    val cycle: Int,
    val selectedPeriod: Int,
    val fixPeriod: String
)

interface AddBillContentActions {
    fun onTitleChange(title: String)
    fun onDateClick()
    fun onAmountChange(amountFormat: TextFieldValue)
    fun onRecurringChange(isRecurring: Boolean)
    fun onCycleChange(cycle: Int)
    fun onSelectedPeriodChange(selectedPeriod: Int)
    fun onFixPeriodChange(period: String)
}