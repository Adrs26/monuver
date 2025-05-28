package com.android.monu.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.presentation.components.NormalAppBar
import com.android.monu.presentation.screen.settings.components.AboutContent
import com.android.monu.presentation.screen.settings.components.AccountPreferenceContent
import com.android.monu.presentation.screen.settings.components.PrivacySecurityContent
import com.android.monu.ui.theme.LightGrey

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = { 
            NormalAppBar(
                title = stringResource(id = R.string.settings), 
                navigateBack = navigateBack
            ) 
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(LightGrey)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AccountPreferenceContent(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp))
            PrivacySecurityContent(modifier = Modifier.padding(horizontal = 16.dp))
            AboutContent(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        navigateBack = {}
    )
}