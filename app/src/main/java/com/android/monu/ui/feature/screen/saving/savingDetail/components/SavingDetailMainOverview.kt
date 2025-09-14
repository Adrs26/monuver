package com.android.monu.ui.feature.screen.saving.savingDetail.components

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
import com.android.monu.R
import com.android.monu.ui.feature.utils.DateHelper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

@Composable
fun SavingDetailMainOverview(
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
                        DateHelper.formatDateToReadable(targetDate),
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
                    text = DateHelper.formatDateToReadable(targetDate),
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

@Composable
private fun getDayDifference(inputDate: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val tanggalInput = LocalDate.parse(inputDate, formatter)
    val today = LocalDate.now()

    val dayDifference = ChronoUnit.DAYS.between(today, tanggalInput).toInt()

    return when {
        dayDifference > 0 -> stringResource(R.string.day_before_target, dayDifference)
        dayDifference < 0 -> stringResource(R.string.day_after_target, -dayDifference)
        else -> stringResource(R.string.today_is_target)
    }
}