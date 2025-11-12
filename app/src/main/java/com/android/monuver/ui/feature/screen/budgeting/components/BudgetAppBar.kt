package com.android.monuver.ui.feature.screen.budgeting.components

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
fun BudgetAppBar(
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
                    painter = painterResource(R.drawable.ic_history),
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