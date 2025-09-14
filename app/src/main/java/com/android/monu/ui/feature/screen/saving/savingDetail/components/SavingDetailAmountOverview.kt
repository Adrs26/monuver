package com.android.monu.ui.feature.screen.saving.savingDetail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.feature.screen.budgeting.components.calculateProgressBar
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.theme.Blue800

@Composable
fun SavingDetailAmountOverview(
    currentAmount: Long,
    targetAmount: Long,
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { calculateProgressBar(currentAmount, targetAmount) },
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(CircleShape)
                        .height(10.dp),
                    color = Blue800,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                    drawStopIndicator = { null }
                )
                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        NumberFormatHelper.formatToPercentageValue(currentAmount, targetAmount)
                    ),
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.saved),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.target),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = NumberFormatHelper.formatToRupiah(currentAmount),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = NumberFormatHelper.formatToRupiah(targetAmount),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                )
            }
        }
    }
}