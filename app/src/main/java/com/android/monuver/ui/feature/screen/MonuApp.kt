package com.android.monuver.ui.feature.screen

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.monuver.R
import com.android.monuver.data.datastore.ThemeSetting
import com.android.monuver.domain.common.CheckAppVersionStatusState
import com.android.monuver.ui.feature.screen.account.accountDetailNavGraph
import com.android.monuver.ui.feature.screen.account.accountNavGraph
import com.android.monuver.ui.feature.screen.analytics.analyticsCategoryTransactionNavGraph
import com.android.monuver.ui.feature.screen.billing.billingNavGraph
import com.android.monuver.ui.feature.screen.billing.payBillNavGraph
import com.android.monuver.ui.feature.screen.budgeting.addBudgetNavGraph
import com.android.monuver.ui.feature.screen.budgeting.budgetDetailNavGraph
import com.android.monuver.ui.feature.screen.budgeting.inactiveBudgetNavGraph
import com.android.monuver.ui.feature.screen.main.MainScreen
import com.android.monuver.ui.feature.screen.onboarding.OnboardingScreen
import com.android.monuver.ui.feature.screen.saving.depositNavGraph
import com.android.monuver.ui.feature.screen.saving.savingNavGraph
import com.android.monuver.ui.feature.screen.saving.withdrawNavGraph
import com.android.monuver.ui.feature.screen.settings.settingsNavGraph
import com.android.monuver.ui.feature.screen.transaction.addTransactionNavGraph
import com.android.monuver.ui.feature.screen.transaction.transactionDetailNavGraph
import com.android.monuver.ui.feature.screen.transaction.transferNavGraph
import com.android.monuver.ui.feature.utils.NavigationAnimation
import com.android.monuver.ui.navigation.Starting

@Composable
fun MonuApp(
    isFirstTime: Boolean,
    checkAppVersionStatus: CheckAppVersionStatusState,
    themeSetting: ThemeSetting,
    onStartCheckAppVersion: () -> Unit,
    onSetFirstTimeToFalse: () -> Unit
) {
    val useDarkTheme = when (themeSetting) {
        ThemeSetting.LIGHT -> false
        ThemeSetting.DARK -> true
        ThemeSetting.SYSTEM -> isSystemInDarkTheme()
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            var status by remember { mutableStateOf(checkAppVersionStatus) }

            LaunchedEffect(Unit) {
                onStartCheckAppVersion()
            }

            LaunchedEffect(checkAppVersionStatus) {
                status = checkAppVersionStatus
            }

            when (status) {
                is CheckAppVersionStatusState.Progress -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(32.dp))
                        Text(
                            text = stringResource(R.string.please_wait_this_process),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
                is CheckAppVersionStatusState.Success -> onSetFirstTimeToFalse()
                is CheckAppVersionStatusState.Error -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.process_failed),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                        )
                        OutlinedButton(
                            onClick = onStartCheckAppVersion,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                        ) {
                            Text(
                                text = stringResource(R.string.refresh),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }
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