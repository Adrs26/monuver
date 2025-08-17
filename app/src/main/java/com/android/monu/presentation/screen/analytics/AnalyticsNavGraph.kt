package com.android.monu.presentation.screen.analytics

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.presentation.screen.analytics.analyticscategorytransaction.AnalyticsCategoryTransactionScreen
import com.android.monu.presentation.screen.analytics.analyticscategorytransaction.AnalyticsCategoryTransactionViewModel
import com.android.monu.presentation.utils.NavigationAnimation
import com.android.monu.ui.navigation.AnalyticsCategoryTransaction
import com.android.monu.ui.navigation.MainAnalyticsCategoryTransaction
import com.android.monu.ui.navigation.MainTransactionDetail
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.analyticsCategoryTransactionNavGraph(
    navController: NavHostController
) {
    navigation<AnalyticsCategoryTransaction>(startDestination = MainAnalyticsCategoryTransaction()) {
        composable<MainAnalyticsCategoryTransaction>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AnalyticsCategoryTransactionViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val category = viewModel.category
            val transactions by viewModel.transactions.collectAsStateWithLifecycle()

            AnalyticsCategoryTransactionScreen(
                category = category,
                transactions = transactions,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToTransactionDetail = { transactionId ->
                    navController.navigate(MainTransactionDetail(id = transactionId))
                }
            )
        }
    }
}