package com.android.monu.presentation.screen.bill

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.monu.presentation.components.CommonFloatingActionButton
import com.android.monu.presentation.screen.bill.components.BillAppBar
import com.android.monu.presentation.screen.bill.components.BillTabRowWithPager

@Composable
fun BillScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddBill: () -> Unit
) {
    Scaffold(
        topBar = {
            BillAppBar(
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton {
                onNavigateToAddBill()
            }
        }
    ) { innerPadding ->
        BillTabRowWithPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}