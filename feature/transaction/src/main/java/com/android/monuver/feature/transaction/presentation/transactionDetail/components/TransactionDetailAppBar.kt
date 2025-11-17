package com.android.monuver.feature.transaction.presentation.transactionDetail.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.monuver.core.presentation.components.DebouncedIconButton
import com.android.monuver.feature.transaction.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionDetailAppBar(
    isTransactionLocked: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToEditTransaction: () -> Unit,
    onRemoveTransaction: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.transaction_detail),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        navigationIcon = {
            DebouncedIconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            if (!isTransactionLocked) {
                DebouncedIconButton(
                    onClick = onNavigateToEditTransaction
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                DebouncedIconButton(
                    onClick = onRemoveTransaction
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}