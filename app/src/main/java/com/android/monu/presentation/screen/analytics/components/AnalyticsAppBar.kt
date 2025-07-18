package com.android.monu.presentation.screen.analytics.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsAppBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.analytics_menu),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        actions = {
            AnalyticsFilterDropdown(
                initialValue = "September",
                modifier = Modifier.padding(end = 16.dp)
            )
            AnalyticsFilterDropdown(
                initialValue = "2025",
                modifier = Modifier.padding(end = 16.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}