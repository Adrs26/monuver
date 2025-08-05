package com.android.monu.presentation.screen.budgeting.budgetingdetail.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.theme.Green600
import com.android.monu.ui.theme.MonuTheme

@Composable
fun BudgetingDetailOverview(
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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.category),
                    modifier = Modifier.weight(1.5f),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = ": Tagihan & utilitas",
                    modifier = Modifier.weight(2.5f),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.budgeting_period),
                    modifier = Modifier.weight(1.5f),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = ": 1 Sep 2025 - 30 Sep 2025",
                    modifier = Modifier.weight(2.5f),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                )
            }
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.maximum_amount),
                    modifier = Modifier.weight(1.5f),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = ": Rp10.000.000",
                    modifier = Modifier.weight(2.5f),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier
                        .weight(0.8f)
                        .clip(CircleShape)
                        .height(10.dp),
                    color = Green600,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Square,
                    gapSize = 0.dp,
                    drawStopIndicator = { null }
                )
                Text(
                    text = "50%",
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
                    text = stringResource(R.string.used),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.remain),
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
                    text = "Rp5.000.000",
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Rp5.000.000",
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetingDetailOverviewPreview() {
    MonuTheme {
        BudgetingDetailOverview()
    }
}