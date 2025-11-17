package com.android.monuver.feature.saving.presentation.savingDetail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.feature.saving.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
internal fun SavingDetailMainOverview(
    title: String,
    targetDate: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            if (isActive) {
                Text(
                    text = stringResource(
                        R.string.save_target_date_information,
                        DateHelper.formatToReadable(targetDate),
                        getDayDifference(targetDate)
                    ),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            } else {
                Text(
                    text = DateHelper.formatToReadable(targetDate),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun getDayDifference(inputDate: String): String {
    val parsedInput = LocalDate.parse(inputDate)
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val dayDifference = today.daysUntil(parsedInput)

    return when {
        dayDifference > 0 -> stringResource(R.string.day_before_target, dayDifference)
        dayDifference < 0 -> stringResource(R.string.day_after_target, -dayDifference)
        else -> stringResource(R.string.today_is_target)
    }
}