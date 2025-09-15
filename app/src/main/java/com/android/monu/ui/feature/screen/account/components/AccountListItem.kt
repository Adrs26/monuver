package com.android.monu.ui.feature.screen.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.account.Account
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import com.android.monu.ui.feature.utils.NumberFormatHelper
import com.android.monu.ui.theme.SoftWhite

@Composable
fun AccountListItem(
    account: Account,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AccountTypeIcon(
            icon = DatabaseCodeMapper.toAccountTypeIcon(account.type),
            backgroundColor = DatabaseCodeMapper.toAccountTypeColor(account.type),
            isActive = account.isActive,
            modifier = Modifier.size(40.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = account.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = stringResource(DatabaseCodeMapper.toAccountType(account.type)),
                modifier = Modifier.padding(top = 4.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        Text(
            text = NumberFormatHelper.formatToRupiah(account.balance),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
fun AccountTypeIcon(
    icon: Int,
    backgroundColor: Color,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(
            color = backgroundColor,
            shape = MaterialTheme.shapes.extraSmall
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = SoftWhite
        )
        if (!isActive) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.BottomEnd),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}