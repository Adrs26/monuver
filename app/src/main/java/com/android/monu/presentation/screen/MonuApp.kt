package com.android.monu.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.monu.presentation.screen.account.accountNavGraph
import com.android.monu.presentation.screen.analytics.analyticsCategoryTransactionNavGraph
import com.android.monu.presentation.screen.budgeting.addBudgetNavGraph
import com.android.monu.presentation.screen.budgeting.budgetDetailNavGraph
import com.android.monu.presentation.screen.budgeting.inactiveBudgetNavGraph
import com.android.monu.presentation.screen.main.MainScreen
import com.android.monu.presentation.screen.settings.settingsNavGraph
import com.android.monu.presentation.screen.transaction.addTransactionNavGraph
import com.android.monu.presentation.screen.transaction.transactionDetailNavGraph
import com.android.monu.presentation.screen.transaction.transferNavGraph
import com.android.monu.ui.navigation.Main
import com.android.monu.ui.theme.SoftWhite
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MonuApp() {
    val systemUiController = rememberSystemUiController()
    SideEffect { systemUiController.setStatusBarColor(color = SoftWhite, darkIcons = true) }

    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = Main
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
    }
}