package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.ui.theme.MonuTheme

@Composable
fun BudgetingOverview(
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
                text = "Budget kamu periode ini",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
            )
            Text(
                text = "Rp1.000.000",
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 24.sp)
            )
            Row(
                modifier = Modifier.padding(top = 24.dp, bottom = 4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Rp200.000",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "telah digunakan",
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
                )
            }
            LinearProgressIndicator(
                progress = { 0.2f },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .height(10.dp),
                color = Color(0xFF43A047),
                trackColor = MaterialTheme.colorScheme.surface,
                strokeCap = StrokeCap.Square,
                gapSize = 0.dp,
                drawStopIndicator = { null }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BudgetingOverviewPreview() {
    MonuTheme {
        BudgetingOverview()
    }
}