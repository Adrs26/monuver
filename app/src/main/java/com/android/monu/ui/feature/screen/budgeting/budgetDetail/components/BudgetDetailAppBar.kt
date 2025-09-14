package com.android.monu.ui.feature.screen.budgeting.budgetDetail.components

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
import com.android.monu.R
import com.android.monu.ui.feature.components.DebouncedIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailAppBar(
    isBudgetActive: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToEditBudget: () -> Unit,
    onRemoveBudget: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.budgeting_detail),
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
            if (isBudgetActive) {
                DebouncedIconButton(
                    onClick = onNavigateToEditBudget
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                DebouncedIconButton(
                    onClick = onRemoveBudget
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