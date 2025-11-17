package com.android.monuver.feature.saving.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.presentation.theme.Blue800
import com.android.monuver.core.presentation.util.calculateProgressBarValue
import com.android.monuver.core.domain.util.DateHelper
import com.android.monuver.core.domain.util.percentageOf
import com.android.monuver.core.domain.util.toRupiah
import com.android.monuver.feature.saving.R

@Composable
internal fun SavingListItem(
    savingState: SavingState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SavingListItemIcon(
            currentAmount = savingState.currentAmount,
            targetAmount = savingState.targetAmount
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = savingState.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = DateHelper.formatToReadable(savingState.targetDate),
                modifier = Modifier.padding(top = 4.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = savingState.currentAmount.toRupiah(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 13.sp
                    )
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(12.dp)
                        .padding(bottom = 2.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Text(
                    text = savingState.targetAmount.toRupiah(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.padding(start = 16.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SavingListItemIcon(
    currentAmount: Long,
    targetAmount: Long
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { calculateProgressBarValue(currentAmount, targetAmount) },
            modifier = Modifier.size(48.dp),
            color = Blue800,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Square,
            gapSize = 0.dp
        )
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraSmall)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    R.string.percentage_value,
                    currentAmount.percentageOf(targetAmount)
                ),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Blue800,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}