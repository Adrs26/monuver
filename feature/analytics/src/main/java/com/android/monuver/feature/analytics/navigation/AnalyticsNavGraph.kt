package com.android.monuver.feature.analytics.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monuver.core.presentation.navigation.AnalyticsCategoryTransaction
import com.android.monuver.core.presentation.navigation.TransactionDetail
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.feature.analytics.presentation.analyticsCategoryTransaction.AnalyticsCategoryTransactionScreen
import com.android.monuver.feature.analytics.presentation.analyticsCategoryTransaction.AnalyticsCategoryTransactionViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.analyticsCategoryTransactionNavGraph(
    navController: NavHostController
) {
    navigation<AnalyticsCategoryTransaction>(startDestination = AnalyticsCategoryTransaction.Main()) {
        composable<AnalyticsCategoryTransaction.Main>(
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
                onNavigateBack = navController::navigateUp,
                onNavigateToTransactionDetail = { transactionId ->
                    navController.navigate(TransactionDetail.Main(transactionId))
                }
            )
        }
    }
}