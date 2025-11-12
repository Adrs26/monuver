package com.android.monuver.ui.feature.screen.billing.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.monuver.R

@Composable
fun BillReminderDialog(
    reminderDaysBeforeDue: Int,
    isReminderBeforeDueDayEnabled: Boolean,
    isReminderForDueBillEnabled: Boolean,
    onDismissRequest: () -> Unit,
    onSettingsApply: (Int, Boolean, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var tempDueDayReminder by remember { mutableIntStateOf(reminderDaysBeforeDue) }
    var tempReminderBeforeDueDay by remember { mutableStateOf(isReminderBeforeDueDayEnabled) }
    var tempReminderForDueBill by remember { mutableStateOf(isReminderForDueBillEnabled) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp)
            ) {
                DueDayReminderSettings(
                    dueDayReminder = tempDueDayReminder,
                    onDueDayReminderChange = { tempDueDayReminder = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                AdvanceReminderSettings(
                    isReminderAfterDueDayEnabled = tempReminderBeforeDueDay,
                    isReminderForDueBillEnabled = tempReminderForDueBill,
                    onReminderAfterDueDayEnableChange = { tempReminderBeforeDueDay = it },
                    onReminderForDueBillEnableChange = { tempReminderForDueBill = it }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp
                            )
                        )
                    }
                    Button(
                        onClick = {
                            onSettingsApply(
                                tempDueDayReminder,
                                tempReminderBeforeDueDay,
                                tempReminderForDueBill
                            )
                            onDismissRequest()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.apply),
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DueDayReminderSettings(
    dueDayReminder: Int,
    onDueDayReminderChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.due_day_reminder),
            style = MaterialTheme.typography.titleMedium
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        listOf(1, 3, 5, 7).forEach { dueDay ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDueDayReminderChange(dueDay) }
            ) {
                RadioButton(
                    selected = dueDayReminder == dueDay,
                    onClick = { onDueDayReminderChange(dueDay) },
                    modifier = Modifier.size(40.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = stringResource(dueDay.toDayReminderStringResource()),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun AdvanceReminderSettings(
    isReminderAfterDueDayEnabled: Boolean,
    isReminderForDueBillEnabled: Boolean,
    onReminderAfterDueDayEnableChange: (Boolean) -> Unit,
    onReminderForDueBillEnableChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.advance_reminder_settings),
            style = MaterialTheme.typography.titleMedium
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isReminderAfterDueDayEnabled,
                onCheckedChange = onReminderAfterDueDayEnableChange,
                modifier = Modifier.size(40.dp),
            )
            Text(
                text = stringResource(R.string.remind_everyday_after_due_day),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isReminderForDueBillEnabled,
                onCheckedChange = onReminderForDueBillEnableChange,
                modifier = Modifier.size(40.dp),
            )
            Text(
                text = stringResource(R.string.remind_everyday_for_due_bill),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 13.sp
                )
            )
        }
    }
}

private fun Int.toDayReminderStringResource(): Int {
    return when (this) {
        1 -> R.string.days_before
        3 -> R.string.three_days_before
        5 -> R.string.five_days_before
        7 -> R.string.weeks_before
        else -> 0
    }
}