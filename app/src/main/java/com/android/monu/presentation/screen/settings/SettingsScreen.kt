package com.android.monu.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.presentation.screen.settings.components.SettingsContent
import com.android.monu.presentation.components.Toolbar
import com.android.monu.ui.theme.LightGrey

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrey)
    ) {
        Toolbar(
            title = stringResource(R.string.settings),
            modifier = Modifier.padding(bottom = 16.dp),
            navigateBack = navigateBack
        )
        SettingsContent(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        navigateBack = {}
    )
}