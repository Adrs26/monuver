package com.android.monuver.ui.feature.screen.account.accountDetail.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.android.monuver.R
import com.android.monuver.ui.feature.components.DebouncedIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailAppBar(
    isAccountActive: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToEditAccount: () -> Unit,
    onDeactivateAccount: () -> Unit,
    onActivateAccount: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.account_detail),
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
            if (isAccountActive) {
                DebouncedIconButton(
                    onClick = onNavigateToEditAccount
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                DebouncedIconButton(
                    onClick = onDeactivateAccount
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                DebouncedIconButton(
                    onClick = onActivateAccount
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock_open_right),
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