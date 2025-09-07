package com.android.monu.ui.feature.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
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
import com.android.monu.ui.feature.screen.saving.savingNavGraph
import com.android.monu.ui.feature.screen.settings.settingsNavGraph
import com.android.monu.ui.feature.screen.transaction.addTransactionNavGraph
import com.android.monu.ui.feature.screen.transaction.transactionDetailNavGraph
import com.android.monu.ui.feature.screen.transaction.transferNavGraph
import com.android.monu.ui.navigation.Main
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MonuApp(
    themeSetting: ThemeSetting
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.background
    val isDarkIcons = when (themeSetting) {
        ThemeSetting.LIGHT -> true
        ThemeSetting.DARK -> false
        else -> !isSystemInDarkTheme()
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = isDarkIcons
        )
    }

    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Main,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        composable<Main> { MainScreen(rootNavController = rootNavController) }
        settingsNavGraph(navController = rootNavController)
        accountNavGraph(navController = rootNavController)
        transactionDetailNavGraph(navController = rootNavController)
        addTransactionNavGraph(navController = rootNavController)
        transferNavGraph(navController = rootNavController)
        budgetDetailNavGraph(navController = rootNavController)
        addBudgetNavGraph(navController = rootNavController)
        inactiveBudgetNavGraph(navController = rootNavController)
        analyticsCategoryTransactionNavGraph(navController = rootNavController)
        billingNavGraph(navController = rootNavController)
        payBillNavGraph(navController = rootNavController)
        savingNavGraph(navController = rootNavController)
    }
}