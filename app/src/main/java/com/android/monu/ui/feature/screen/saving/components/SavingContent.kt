package com.android.monu.ui.feature.screen.saving.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.save.Save
import com.android.monu.ui.feature.components.CommonLottieAnimation

@Composable
fun SavingContent(
    totalCurrentAmount: Long,
    saves: List<Save>,
    onNavigateToSaveDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        saves.isEmpty() -> {
            SaveEmptyListContent(
                totalCurrentAmount = totalCurrentAmount,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
        else -> {
            SaveListContent(
                totalCurrentAmount = totalCurrentAmount,
                saves = saves,
                onNavigateToSaveDetail = onNavigateToSaveDetail,
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SaveListContent(
    totalCurrentAmount: Long,
    saves: List<Save>,
    onNavigateToSaveDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            SaveOverview(
                totalCurrentAmount = totalCurrentAmount,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.list_active_save),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(
            count = saves.size,
            key = { index -> saves[index].id }
        ) { index ->
            val saveState = saves[index]

            SaveListItem(
                save = saveState,
                modifier = Modifier.clickable { onNavigateToSaveDetail(saveState.id) }
            )
        }
    }
}

@Composable
fun SaveEmptyListContent(
    totalCurrentAmount: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SaveOverview(totalCurrentAmount = totalCurrentAmount)
        Text(
            text = stringResource(R.string.list_active_save),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CommonLottieAnimation(lottieAnimation = R.raw.empty)
        }
    }
}