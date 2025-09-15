package com.android.monu.ui.feature.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.monu.ui.theme.MonuTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        setContent {
            WindowInsets.statusBars

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                RequestHighRefreshRate()
            }

            val viewModel = koinViewModel<MainViewModel>()
            val themeSetting by viewModel.themeSetting.collectAsStateWithLifecycle()

            MonuTheme(themeSetting = themeSetting) {
                MonuApp(themeSetting = themeSetting)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
private fun RequestHighRefreshRate() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val activity = context as? Activity ?: return@LaunchedEffect

        val window = activity.window
        val layoutParams = window.attributes

        val supportedRefreshRates = activity.display?.supportedModes?.map { it.refreshRate } ?: emptyList()
        val highestRefreshRate = supportedRefreshRates.maxOrNull() ?: 60.0f

        layoutParams.preferredRefreshRate = highestRefreshRate

        window.attributes = layoutParams
    }
}