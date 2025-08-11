package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.utils.NumberFormatHelper
import com.android.monu.presentation.utils.debouncedClickable
import com.android.monu.ui.theme.MonuTheme

@Composable
fun HomeBalanceOverview(
    totalBalance: Long,
    onNavigateToAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.total_account_balance),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
            )
            Text(
                text = NumberFormatHelper.formatToRupiah(totalBalance),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 24.sp)
            )
        }
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.extraSmall
                )
                .size(48.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .debouncedClickable { onNavigateToAccount() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_account_balance_wallet),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBalanceOverviewPreview() {
    MonuTheme {
        HomeBalanceOverview(
            totalBalance = 0,
            onNavigateToAccount = {}
        )
    }
}