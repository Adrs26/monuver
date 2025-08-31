package com.android.monu.ui.feature.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.monu.R
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.ui.feature.components.CommonAppBar
import com.android.monu.ui.feature.screen.settings.components.SettingsApplicationData
import com.android.monu.ui.feature.screen.settings.components.SettingsPreference
import com.android.monu.ui.feature.screen.settings.components.SettingsSecurity
import com.android.monu.ui.feature.screen.settings.components.SettingsThemeDialog

@Composable
fun SettingsScreen(
    themeSetting: ThemeSetting,
    onNavigateBack: () -> Unit,
    onThemeChange: (ThemeSetting) -> Unit
) {
    var checked by remember { mutableStateOf(true) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = stringResource(R.string.settings),
                onNavigateBack = onNavigateBack
            ) 
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsPreference(
                isNotificationEnabled = checked,
                themeSetting = themeSetting,
                onNotificationClicked = {  },
                onThemeClicked = { showThemeDialog = true },
                modifier = Modifier.padding(top = 8.dp)
            )
            SettingsApplicationData(
                onExportDataClicked = {  },
                onBackupDataClicked = {  },
                onRestoreDataClicked = {  },
                onDeleteDataClicked = {  }
            )
            SettingsSecurity(
                onPinAuthenticationClicked = {  },
                onFingerprintAuthenticationClicked = {  }
            )
        }
    }

    if (showThemeDialog) {
        SettingsThemeDialog(
            themeSetting = themeSetting,
            onThemeChange = onThemeChange,
            onDismissRequest = { showThemeDialog = false }
        )
    }
}