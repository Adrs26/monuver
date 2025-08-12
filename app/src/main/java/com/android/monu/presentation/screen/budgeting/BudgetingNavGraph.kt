package com.android.monu.presentation.screen.budgeting

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.presentation.screen.budgeting.addbudgeting.AddBudgetingActions
import com.android.monu.presentation.screen.budgeting.addbudgeting.AddBudgetingScreen
import com.android.monu.presentation.screen.budgeting.addbudgeting.AddBudgetingViewModel
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingCategoryScreen
import com.android.monu.presentation.screen.budgeting.addbudgeting.components.AddBudgetingContentState
import com.android.monu.presentation.screen.budgeting.budgetingdetail.BudgetingDetailScreen
import com.android.monu.presentation.screen.budgeting.budgetingdetail.BudgetingDetailState
import com.android.monu.presentation.screen.budgeting.budgetingdetail.BudgetingDetailViewModel
import com.android.monu.presentation.utils.NavigationAnimation
import com.android.monu.presentation.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddBudgeting
import com.android.monu.ui.navigation.AddBudgetingCategory
import com.android.monu.ui.navigation.BudgetingDetail
import com.android.monu.ui.navigation.MainAddBudgeting
import com.android.monu.ui.navigation.MainBudgetingDetail
import com.android.monu.ui.navigation.MainTransactionDetail
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addBudgetingNavGraph(
    navController: NavHostController
) {
    navigation<AddBudgeting>(startDestination = MainAddBudgeting) {
        composable<MainAddBudgeting>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddBudgetingViewModel>(navController)
            val category by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val addBudgetingActions = object : AddBudgetingActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToCategory() {
                    navController.navigate(AddBudgetingCategory)
                }

                override fun onAddNewBudgeting(budgetingState: AddBudgetingContentState) {
                    viewModel.createNewBudgeting(budgetingState)
                }
            }

            AddBudgetingScreen(
                category = category,
                addResult = addResult,
                budgetingActions = addBudgetingActions
            )
        }

        composable<AddBudgetingCategory>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddBudgetingViewModel>(navController)

            AddBudgetingCategoryScreen(
                onNavigateBack = { navController.navigateUp() },
                onCategorySelect = viewModel::changeTransactionCategory
            )
        }
    }
}

fun NavGraphBuilder.budgetingDetailNavGraph(
    navController: NavHostController
) {
    navigation<BudgetingDetail>(startDestination = MainBudgetingDetail()) {
        composable<MainBudgetingDetail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<BudgetingDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val budgeting by viewModel.budgeting.collectAsStateWithLifecycle()
            val transactions by viewModel.transactions.collectAsStateWithLifecycle()

            budgeting?.let { budgeting ->
                val budgetingState = BudgetingDetailState(
                    id = budgeting.id,
                    category = budgeting.category,
                    period = budgeting.period,
                    startDate = budgeting.startDate,
                    endDate = budgeting.endDate,
                    maxAmount = budgeting.maxAmount,
                    usedAmount = budgeting.usedAmount
                )

                BudgetingDetailScreen(
                    budgetingState = budgetingState,
                    transactions = transactions,
                    onNavigateBack = { navController.navigateUp() },
                    onNavigateToTransactionDetail = { transactionId ->
                        navController.navigate(MainTransactionDetail(id = transactionId))
                    },
                    onRemoveBudgeting = viewModel::deleteBudgeting
                )
            }
        }
    }
}