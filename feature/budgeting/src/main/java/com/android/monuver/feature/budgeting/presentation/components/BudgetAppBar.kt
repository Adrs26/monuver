package com.android.monuver.feature.budgeting.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.android.monuver.core.presentation.components.DebouncedIconButton
import com.android.monuver.feature.budgeting.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetAppBar(
    onNavigateToInactiveBudget: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.budgeting_menu),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        actions = {
            DebouncedIconButton(
                onClick = onNavigateToInactiveBudget
            ) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}