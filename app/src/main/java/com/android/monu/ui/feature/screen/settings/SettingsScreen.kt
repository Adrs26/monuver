package com.android.monu.ui.feature.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.settings.components.AboutContent
import com.android.monu.ui.feature.screen.settings.components.AccountPreferenceContent
import com.android.monu.ui.feature.screen.settings.components.PrivacySecurityContent

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.settings),
                onNavigateBack = onNavigateBack
            ) 
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AccountPreferenceContent(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp))
            PrivacySecurityContent(modifier = Modifier.padding(horizontal = 16.dp))
            AboutContent(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}