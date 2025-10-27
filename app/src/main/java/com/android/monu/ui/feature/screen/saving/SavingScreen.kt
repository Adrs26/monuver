package com.android.monu.ui.feature.screen.saving

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.monu.domain.model.SavingState
import com.android.monu.ui.feature.components.CommonFloatingActionButton
import com.android.monu.ui.feature.screen.saving.components.SavingAppBar
import com.android.monu.ui.feature.screen.saving.components.SavingContent

@Composable
fun SavingScreen(
    totalCurrentAmount: Long,
    savings: List<SavingState>,
    savingActions: SavingActions
) {
    Scaffold(
        topBar = {
            SavingAppBar(
                onNavigateBack = savingActions::onNavigateBack,
                onNavigateToInactiveSaving = savingActions::onNavigateToInactiveSaving
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton { savingActions.onNavigateToAddSaving() }
        }
    ) { innerPadding ->
        SavingContent(
            totalCurrentAmount = totalCurrentAmount,
            savings = savings,
            onNavigateToSavingDetail = savingActions::onNavigateToSavingDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

interface SavingActions {
    fun onNavigateBack()
    fun onNavigateToInactiveSaving()
    fun onNavigateToAddSaving()
    fun onNavigateToSavingDetail(savingId: Long)
}