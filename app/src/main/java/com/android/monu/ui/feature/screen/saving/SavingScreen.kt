package com.android.monu.ui.feature.screen.saving

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.monu.domain.model.save.Save
import com.android.monu.ui.feature.components.CommonFloatingActionButton
import com.android.monu.ui.feature.screen.saving.components.SaveAppBar
import com.android.monu.ui.feature.screen.saving.components.SavingContent

@Composable
fun SavingScreen(
    totalCurrentAmount: Long,
    saves: List<Save>,
    saveActions: SaveActions
) {
    Scaffold(
        topBar = {
            SaveAppBar(
                onNavigateBack = saveActions::onNavigateBack
            )
        },
        floatingActionButton = {
            CommonFloatingActionButton { saveActions.onNavigateToAddSave() }
        }
    ) { innerPadding ->
        SavingContent(
            totalCurrentAmount = totalCurrentAmount,
            saves = saves,
            onNavigateToSaveDetail = saveActions::onNavigateToSaveDetail,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

interface SaveActions {
    fun onNavigateBack()
    fun onNavigateToAddSave()
    fun onNavigateToSaveDetail(saveId: Long)
}