package com.android.monuver.feature.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.core.presentation.theme.Blue800
import com.android.monuver.core.presentation.theme.Charcoal
import com.android.monuver.core.presentation.theme.Green600
import com.android.monuver.core.presentation.theme.Red600
import com.android.monuver.core.presentation.util.debouncedClickable
import com.android.monuver.feature.home.R

@Composable
internal fun HomeMenuButtonBar(
    onNavigateToAddIncomeTransaction: () -> Unit,
    onNavigateToAddExpenseTransaction: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToAddBudget: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeMenuButton(
            icon = Icons.Default.ArrowUpward,
            iconColor = Green600,
            title = stringResource(R.string.income),
            onClick = onNavigateToAddIncomeTransaction,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
        HomeMenuButton(
            icon = Icons.Default.ArrowDownward,
            iconColor = Red600,
            title = stringResource(R.string.expense),
            onClick = onNavigateToAddExpenseTransaction,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
        HomeMenuButton(
            icon = Icons.AutoMirrored.Default.CompareArrows,
            iconColor = Blue800,
            title = stringResource(R.string.transfer),
            onClick = onNavigateToTransfer,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
        HomeMenuButton(
            icon = Icons.Outlined.PieChartOutline,
            iconColor = Charcoal,
            title = stringResource(R.string.budgeting_menu),
            onClick = onNavigateToAddBudget,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun HomeMenuButton(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .size(48.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .debouncedClickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = iconColor
            )
        }
        Text(
            text = title,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
        )
    }
}
