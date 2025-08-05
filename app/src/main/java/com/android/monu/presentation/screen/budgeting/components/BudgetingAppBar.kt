package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.monu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetingAppBar(
    onHistoryClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Budget",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        actions = {
            IconButton(
                onClick = onHistoryClick
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

@Preview(showBackground = true)
@Composable
fun BudgetingAppBarPreview() {
    BudgetingAppBar(
        onHistoryClick = { }
    )
}