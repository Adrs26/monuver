package com.android.monu.presentation.screen.budgeting.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.monu.presentation.components.NormalAppBar
import com.android.monu.presentation.screen.budgeting.components.BudgetingList

@Composable
fun BudgetingHistoryScreen(
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            NormalAppBar(
                title = "Riwayat",
                navigateBack = navigateBack
            )
        }
    ) { innerPadding ->
        BudgetingList(modifier = Modifier.padding(innerPadding))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBudgetingHistoryScreen() {
    BudgetingHistoryScreen(
        navigateBack = {}
    )
}