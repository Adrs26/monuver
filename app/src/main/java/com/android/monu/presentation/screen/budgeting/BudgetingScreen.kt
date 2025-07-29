package com.android.monu.presentation.screen.budgeting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.screen.budgeting.components.BudgetingAppBar
import com.android.monu.presentation.screen.budgeting.components.BudgetingListItem
import com.android.monu.presentation.screen.budgeting.components.BudgetingOverview
import com.android.monu.ui.theme.MonuTheme

@Composable
fun BudgetingScreen(
    onHistoryClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BudgetingAppBar(
                onHistoryClick = onHistoryClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            BudgetingOverview(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            Text(
                text = "Daftar budget aktif",
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp, start = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            BudgetingListItem(
                modifier = Modifier.clickable {
                    onItemClick()
                }
            )
            BudgetingListItem()
            BudgetingListItem()
            BudgetingListItem()
            BudgetingListItem()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetingScreenPreview() {
    MonuTheme {
        BudgetingScreen(
            onHistoryClick = { },
            onItemClick = { }
        )
    }
}