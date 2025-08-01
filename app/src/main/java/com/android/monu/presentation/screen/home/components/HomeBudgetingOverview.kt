package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.presentation.utils.debouncedClickable
import com.android.monu.ui.theme.Green600

@Composable
fun HomeBudgetingOverview(
    modifier: Modifier = Modifier,
    onNavigateToBudgeting: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rekap budget periode ini",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Lihat budget",
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .debouncedClickable { onNavigateToBudgeting() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 0.2f },
                    modifier = Modifier.size(128.dp),
                    color = Green600,
                    strokeWidth = 8.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = "20%",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Green600,
                        fontSize = 20.sp
                    )
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 48.dp)
            ) {
                Text(
                    text = "Saldo maksimal",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
                Text(
                    text = "Rp10.000.000",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Saldo digunakan",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
                Text(
                    text = "Rp2.000.000",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Saldo tersisa",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
                Text(
                    text = "Rp8.000.000",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}