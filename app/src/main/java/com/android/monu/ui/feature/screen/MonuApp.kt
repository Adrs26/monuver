package com.android.monu.ui.feature.screen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.monu.data.datastore.ThemeSetting
import com.android.monu.ui.feature.screen.account.accountNavGraph
import com.android.monu.ui.feature.screen.analytics.analyticsCategoryTransactionNavGraph
import com.android.monu.ui.feature.screen.billing.billingNavGraph
import com.android.monu.ui.feature.screen.billing.payBillNavGraph
import com.android.monu.ui.feature.screen.budgeting.addBudgetNavGraph
import com.android.monu.ui.feature.screen.budgeting.budgetDetailNavGraph
import com.android.monu.ui.feature.screen.budgeting.inactiveBudgetNavGraph
import com.android.monu.ui.feature.screen.main.MainScreen
import com.android.monu.ui.feature.screen.saving.depositNavGraph
import com.android.monu.ui.feature.screen.saving.savingNavGraph
import com.android.monu.ui.feature.screen.saving.withdrawNavGraph
import com.android.monu.ui.feature.screen.settings.settingsNavGraph
import com.android.monu.ui.feature.screen.transaction.addTransactionNavGraph
import com.android.monu.ui.feature.screen.transaction.transactionDetailNavGraph
import com.android.monu.ui.feature.screen.transaction.transferNavGraph
import com.android.monu.ui.navigation.Starting

@Composable
fun MonuApp(
    themeSetting: ThemeSetting
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
        startDestination = Starting.Main,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable<Starting.Main> { MainScreen(rootNavController) }

        settingsNavGraph(rootNavController)

        accountNavGraph(rootNavController)

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