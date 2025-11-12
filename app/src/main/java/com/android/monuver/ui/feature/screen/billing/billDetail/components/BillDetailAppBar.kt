package com.android.monuver.ui.feature.screen.billing.billDetail.components

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
fun BillDetailAppBar(
    isPaid: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToEditBill: () -> Unit,
    onRemoveBill: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.bill_detail),
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
            if (!isPaid) {
                DebouncedIconButton(
                    onClick = onNavigateToEditBill
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                DebouncedIconButton(
                    onClick = onRemoveBill
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
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