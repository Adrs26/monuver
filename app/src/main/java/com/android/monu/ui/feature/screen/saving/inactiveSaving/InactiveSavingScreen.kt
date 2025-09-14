package com.android.monu.ui.feature.screen.saving.inactiveSaving

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.domain.model.saving.Saving
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.components.CommonLottieAnimation
import com.android.monu.ui.feature.screen.saving.components.SavingListItem
import com.android.monu.ui.feature.utils.debouncedClickable

@Composable
fun InactiveSavingScreen(
    savings: List<Saving>,
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (savings.isEmpty()) {
                CommonLottieAnimation(lottieAnimation = R.raw.empty)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(
                        count = savings.size,
                        key = { index -> savings[index].id }
                    ) { index ->
                        val saveState = savings[index]

                        SavingListItem(
                            saving = saveState,
                            modifier = Modifier.debouncedClickable { onNavigateToSavingDetail(saveState.id) }
                        )
                    }
                }
            }
        }
    }
}