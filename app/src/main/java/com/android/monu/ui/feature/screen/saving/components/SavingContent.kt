package com.android.monu.ui.feature.screen.saving.components

import androidx.compose.foundation.background
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
import com.android.monu.domain.model.SavingState
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun SavingContent(
    totalCurrentAmount: Long,
    savings: List<SavingState>,
    onNavigateToSavingDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        savings.isEmpty() -> {
            SavingEmptyListContent(
                totalCurrentAmount = totalCurrentAmount,
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
        else -> {
            SavingListContent(
                totalCurrentAmount = totalCurrentAmount,
                savings = savings,
                onNavigateToSavingDetail = onNavigateToSavingDetail,
                modifier = modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SavingListContent(
    totalCurrentAmount: Long,
    savings: List<SavingState>,
    onNavigateToSavingDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            SavingOverview(
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
            count = savings.size,
            key = { index -> savings[index].id }
        ) { index ->
            val savingState = savings[index]

            SavingListItem(
                savingState = savingState,
                modifier = Modifier.debouncedClickable { onNavigateToSavingDetail(savingState.id) }
            )
        }
    }
}

@Composable
fun SavingEmptyListContent(
    totalCurrentAmount: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SavingOverview(totalCurrentAmount = totalCurrentAmount)
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
            CommonLottieAnimation(lottieAnimation = R.raw.empty_box)
        }
    }
}