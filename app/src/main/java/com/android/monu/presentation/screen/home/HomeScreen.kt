package com.android.monu.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.presentation.screen.home.components.HomeBalance
import com.android.monu.presentation.screen.home.components.HomeMenuButtonBar
import com.android.monu.presentation.screen.home.components.HomeRecentTransactions
import com.android.monu.presentation.screen.home.components.HomeTopBar
import com.android.monu.ui.theme.LightGrey

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToSettings: () -> Unit,
    navigateToTransactions: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrey)
            .padding(vertical = 8.dp)
    ) {
        HomeTopBar(
            modifier = Modifier.padding(horizontal = 12.dp),
            toSettings = navigateToSettings
        )
        HomeBalance(modifier = Modifier.padding(16.dp))
        HomeMenuButtonBar(modifier = Modifier.padding(horizontal = 12.dp))
        HomeRecentTransactions(
            modifier = Modifier.padding(16.dp),
            toTransactions = navigateToTransactions
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navigateToSettings = {},
        navigateToTransactions = {}
    )
}