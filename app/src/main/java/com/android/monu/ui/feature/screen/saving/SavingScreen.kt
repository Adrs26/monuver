package com.android.monu.ui.feature.screen.saving

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.android.monu.ui.feature.components.CommonFloatingActionButton
import com.android.monu.ui.feature.screen.saving.components.SaveAppBar
import com.android.monu.ui.feature.screen.saving.components.SaveListItem
import com.android.monu.ui.feature.screen.saving.components.SaveOverview

@Composable
fun SavingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSaveDetail: () -> Unit
) {
    Scaffold(
        topBar = {
            SaveAppBar(
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton {  }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SaveOverview(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            Text(
                text = stringResource(R.string.list_active_save),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            SaveListItem(
                modifier = Modifier.clickable { onNavigateToSaveDetail() }
            )
            SaveListItem()
            SaveListItem()
        }
    }
}