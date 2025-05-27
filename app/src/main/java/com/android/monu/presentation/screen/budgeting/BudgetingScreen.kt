package com.android.monu.presentation.screen.budgeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.screen.budgeting.components.BudgetingAppBar
import com.android.monu.presentation.screen.budgeting.components.BudgetingFilterBar
import com.android.monu.presentation.screen.budgeting.components.BudgetingHeader
import com.android.monu.presentation.screen.budgeting.components.BudgetingList
import com.android.monu.ui.theme.Blue
import com.android.monu.ui.theme.LightGrey

@Composable
fun BudgetingScreen(
    onHistoryClick: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            BudgetingAppBar(
                navigateBack = navigateBack,
                onHistoryClick = onHistoryClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
                shape = CircleShape,
                containerColor = Blue
            ) {
                Icon(
                    imageVector =  Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrey)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BudgetingHeader(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp))
            BudgetingFilterBar(modifier = Modifier.padding(horizontal = 16.dp))
            BudgetingList(modifier = Modifier.padding(horizontal = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetingScreenPreview() {
    BudgetingScreen(
        onHistoryClick = { },
        navigateBack = { }
    )
}