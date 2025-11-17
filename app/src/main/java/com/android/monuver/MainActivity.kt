package com.android.monuver

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.monuver.core.domain.common.CheckAppVersionStatusState
import com.android.monuver.core.domain.common.ThemeState
import com.android.monuver.core.presentation.navigation.NotificationNavigator
import com.android.monuver.core.presentation.navigation.Starting
import com.android.monuver.core.presentation.theme.MonuTheme
import com.android.monuver.core.presentation.util.AuthenticationManager
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.feature.account.navigation.accountDetailNavGraph
import com.android.monuver.feature.account.navigation.accountNavGraph
import com.android.monuver.feature.analytics.navigation.analyticsCategoryTransactionNavGraph
import com.android.monuver.feature.billing.navigation.billingNavGraph
import com.android.monuver.feature.billing.navigation.payBillNavGraph
import com.android.monuver.feature.budgeting.navigation.addBudgetNavGraph
import com.android.monuver.feature.budgeting.navigation.budgetDetailNavGraph
import com.android.monuver.feature.budgeting.navigation.inactiveBudgetNavGraph
import com.android.monuver.feature.main.MainScreen
import com.android.monuver.feature.saving.navigation.depositNavGraph
import com.android.monuver.feature.saving.navigation.savingNavGraph
import com.android.monuver.feature.saving.navigation.withdrawNavGraph
import com.android.monuver.feature.settings.navigation.settingsNavGraph
import com.android.monuver.feature.transaction.navigation.addTransactionNavGraph
import com.android.monuver.feature.transaction.navigation.transactionDetailNavGraph
import com.android.monuver.feature.transaction.navigation.transferNavGraph
import com.android.monuver.onboarding.CheckAppVersionScreen
import com.android.monuver.onboarding.OnboardingScreen
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { RequestHighRefreshRate() }

            val viewModel = koinViewModel<MainViewModel>()
            val isFirstTime by viewModel.isFirstTime.collectAsStateWithLifecycle()
            val checkAppVersionStatus by viewModel.checkAppVersionStatus.collectAsStateWithLifecycle()
            val themeState by viewModel.themeState.collectAsStateWithLifecycle()
            val isAuthenticationEnabled by viewModel.isAuthenticationEnabled.collectAsStateWithLifecycle()
            val isAuthenticated by viewModel.isAuthenticated.collectAsStateWithLifecycle()

            val activity = LocalActivity.current as FragmentActivity

            LaunchedEffect(Unit) {
                delay(500)
                if (isAuthenticationEnabled) {
                    AuthenticationManager.showBiometricPrompt(
                        activity = activity,
                        onAuthSuccess = { viewModel.setAuthenticationStatus(true) },
                        onAuthFailed = {},
                        onAuthError = {}
                    )
                } else {
                    viewModel.setAuthenticationStatus(true)
                }
            }

            MonuTheme(themeState) {
                if (isAuthenticated) {
                    App(
                        isFirstTime = isFirstTime,
                        checkAppVersionStatus = checkAppVersionStatus,
                        themeState = themeState,
                        onStartCheckAppVersion = viewModel::checkAppVersion,
                        onSetFirstTimeToFalse = viewModel::setIsFirstTimeToFalse
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

@Composable
fun App(
    isFirstTime: Boolean,
    checkAppVersionStatus: CheckAppVersionStatusState,
    themeState: ThemeState,
    onStartCheckAppVersion: () -> Unit,
    onSetFirstTimeToFalse: () -> Unit
) {
    val useDarkTheme = when (themeState) {
        ThemeState.Light -> false
        ThemeState.Dark -> true
        ThemeState.System -> isSystemInDarkTheme()
    }
    SystemAppearance(useDarkTheme)

    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = if (isFirstTime) Starting.Onboarding else Starting.Main,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        startingNavGraph(
            navController = rootNavController,
            checkAppVersionStatus = checkAppVersionStatus,
            onStartCheckAppVersion = onStartCheckAppVersion,
            onSetFirstTimeToFalse = onSetFirstTimeToFalse
        )
        settingsNavGraph(rootNavController)
        accountNavGraph(rootNavController)
        accountDetailNavGraph(rootNavController)
        transactionDetailNavGraph(rootNavController)
        addTransactionNavGraph(rootNavController)
        transferNavGraph(rootNavController)
        budgetDetailNavGraph(rootNavController)
        addBudgetNavGraph(rootNavController)
        inactiveBudgetNavGraph(rootNavController)
        analyticsCategoryTransactionNavGraph(rootNavController)
        billingNavGraph(rootNavController)
        payBillNavGraph(rootNavController)
        savingNavGraph(rootNavController)
        depositNavGraph(rootNavController)
        withdrawNavGraph(rootNavController)
    }
}

private fun NavGraphBuilder.startingNavGraph(
    navController: NavHostController,
    checkAppVersionStatus: CheckAppVersionStatusState,
    onStartCheckAppVersion: () -> Unit,
    onSetFirstTimeToFalse: () -> Unit
) {
    composable<Starting.Onboarding> {
        OnboardingScreen(
            onFinishOnboarding = {
                navController.navigate(Starting.CheckAppVersion) {
                    popUpTo(Starting.Onboarding) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<Starting.CheckAppVersion> {
        CheckAppVersionScreen(
            checkAppVersionStatusState = checkAppVersionStatus,
            onStartCheckAppVersion = onStartCheckAppVersion,
            onSetFirstTimeToFalse = onSetFirstTimeToFalse
        )
    }

    composable<Starting.Main>(
        enterTransition = { NavigationAnimation.enter },
        exitTransition = { NavigationAnimation.exit },
        popEnterTransition = { NavigationAnimation.popEnter },
        popExitTransition = { NavigationAnimation.popExit }
    ) {
        MainScreen(
            rootNavController = navController
        )
    }
}

@Composable
private fun SystemAppearance(isDark: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(isDark) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }
}

class NotificationNavigatorImpl : NotificationNavigator {
    override fun openMainActivity(context: Context): Intent {
        return Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}