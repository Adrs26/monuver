package com.android.monu.ui.feature.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.monu.R
import com.android.monu.ui.feature.utils.AuthenticationManager
import com.android.monu.ui.feature.utils.showMessageWithToast
import com.android.monu.ui.theme.MonuTheme
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

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
            val isAuthenticationEnabled by viewModel.isAuthenticationEnabled.collectAsStateWithLifecycle()
            val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()

            val activity = LocalActivity.current as FragmentActivity

            LaunchedEffect(Unit) {
                delay(500)
                if (isAuthenticationEnabled) {
                    AuthenticationManager.showBiometricPrompt(
                        activity = activity,
                        onAuthSuccess = { viewModel.setAuthenticationStatus(true) },
                        onAuthFailed = {
                            getString(R.string.fingerprint_not_matched)
                                .showMessageWithToast(this@MainActivity)
                        },
                        onAuthError = {}
                    )
                } else {
                    viewModel.setAuthenticationStatus(true)
                }
            }

            MonuTheme(themeSetting = themeSetting) {
                if (isAuthenticated) {
                    MonuApp(
                        themeSetting = themeSetting
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
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