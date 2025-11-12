package com.android.monuver.ui.feature.screen.saving.savingDetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monuver.R
import com.android.monuver.ui.theme.Green600
import com.android.monuver.ui.theme.Red600

@Composable
fun SavingDetailButtonBar(
    onNavigateToDeposit: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SavingDetailButton(
            text = stringResource(R.string.add_amount),
            icon = R.drawable.ic_arrow_upward,
            color = Green600,
            onClick = onNavigateToDeposit,
            modifier = Modifier.weight(1f)
        )
        SavingDetailButton(
            text = stringResource(R.string.withdraw_amount),
            icon = R.drawable.ic_arrow_downward,
            color = Red600,
            onClick = onNavigateToWithdraw,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SavingDetailButton(
    text: String,
    icon: Int,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                color = color,
                fontSize = 11.sp
            )
        )
    }
}