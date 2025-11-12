package com.android.monuver.ui.feature.screen.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.ui.feature.utils.debouncedClickable
import com.android.monuver.ui.theme.Blue800
import com.android.monuver.ui.theme.Charcoal
import com.android.monuver.ui.theme.Green600
import com.android.monuver.ui.theme.Red600

@Composable
fun HomeMenuButtonBar(
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
            icon = painterResource(R.drawable.ic_arrow_upward),
            iconColor = Green600,
            title = stringResource(R.string.income),
            onClick = onNavigateToAddIncomeTransaction,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
        HomeMenuButton(
            icon = painterResource(R.drawable.ic_arrow_downward),
            iconColor = Red600,
            title = stringResource(R.string.expense),
            onClick = onNavigateToAddExpenseTransaction,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
        HomeMenuButton(
            icon = painterResource(R.drawable.ic_compare_arrows),
            iconColor = Blue800,
            title = stringResource(R.string.transfer),
            onClick = onNavigateToTransfer,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
        )
        HomeMenuButton(
            icon = painterResource(R.drawable.ic_budgeting_outlined),
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
    icon: Painter,
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
                painter = icon,
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
