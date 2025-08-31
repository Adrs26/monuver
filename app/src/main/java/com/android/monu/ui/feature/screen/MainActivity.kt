package com.android.monu.ui.feature.screen

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.monu.ui.theme.MonuTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            val themeSetting by viewModel.themeSetting.collectAsStateWithLifecycle()

            MonuTheme(themeSetting = themeSetting) {
                MonuApp(themeSetting = themeSetting)
            }
        }
    }
}