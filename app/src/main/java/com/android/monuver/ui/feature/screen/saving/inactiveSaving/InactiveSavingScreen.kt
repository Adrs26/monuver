package com.android.monuver.ui.feature.screen.saving.inactiveSaving

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monuver.R
import com.android.monuver.domain.model.SavingState
import com.android.monuver.ui.feature.components.CommonAppBar
import com.android.monuver.ui.feature.components.CommonLottieAnimation
import com.android.monuver.ui.feature.screen.saving.components.SavingListItem
import com.android.monuver.ui.feature.utils.debouncedClickable

@Composable
fun InactiveSavingScreen(
    savings: List<SavingState>,
    onNavigateBack: () -> Unit,
    onNavigateToSavingDetail: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.inactive_save),
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        if (savings.isEmpty()) {
            CommonLottieAnimation(
                lottieAnimation = R.raw.empty_box,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(
                    count = savings.size,
                    key = { index -> savings[index].id }
                ) { index ->
                    val saveState = savings[index]

                    SavingListItem(
                        savingState = saveState,
                        modifier = Modifier.debouncedClickable { onNavigateToSavingDetail(saveState.id) }
                    )
                }
            }
        }
    }
}