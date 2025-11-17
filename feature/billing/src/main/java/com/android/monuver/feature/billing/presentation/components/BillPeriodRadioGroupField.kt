package com.android.monuver.feature.billing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.feature.billing.R

@Composable
internal fun BillPeriodRadioGroupField(
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
            text = stringResource(R.string.bill_period),
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
                    text = stringResource(R.string.unlimited),
                    style = MaterialTheme.typography.labelMedium.copy(
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
                    text = stringResource(R.string.times),
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}