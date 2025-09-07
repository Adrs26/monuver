package com.android.monu.ui.feature.screen.saving.savedetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailAmountOverview
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailAppBar
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailBottomBar
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailButtonBar
import com.android.monu.ui.feature.screen.saving.savedetail.components.SaveDetailMainOverview

@Composable
fun SaveDetailScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            SaveDetailAppBar(
                title = "Detail tabungan",
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            SaveDetailBottomBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SaveDetailMainOverview(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            SaveDetailAmountOverview(
                modifier = Modifier.padding(16.dp)
            )
            SaveDetailButtonBar(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = stringResource(R.string.transaction_history),
                modifier = Modifier.padding(top = 24.dp, start = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}