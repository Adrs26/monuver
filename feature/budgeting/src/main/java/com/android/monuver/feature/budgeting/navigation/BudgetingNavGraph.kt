package com.android.monuver.feature.budgeting.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monuver.core.presentation.navigation.AddBudget
import com.android.monuver.core.presentation.navigation.BudgetDetail
import com.android.monuver.core.presentation.navigation.InactiveBudget
import com.android.monuver.core.presentation.navigation.TransactionDetail
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.core.presentation.util.sharedKoinViewModel
import com.android.monuver.feature.budgeting.presentation.addBudget.AddBudgetScreen
import com.android.monuver.feature.budgeting.presentation.addBudget.AddBudgetViewModel
import com.android.monuver.feature.budgeting.presentation.addBudget.components.AddBudgetCategoryScreen
import com.android.monuver.feature.budgeting.presentation.budgetDetail.BudgetDetailActions
import com.android.monuver.feature.budgeting.presentation.budgetDetail.BudgetDetailScreen
import com.android.monuver.feature.budgeting.presentation.budgetDetail.BudgetDetailViewModel
import com.android.monuver.feature.budgeting.presentation.editBudget.EditBudgetScreen
import com.android.monuver.feature.budgeting.presentation.editBudget.EditBudgetViewModel
import com.android.monuver.feature.budgeting.presentation.inactiveBudget.InactiveBudgetScreen
import com.android.monuver.feature.budgeting.presentation.inactiveBudget.InactiveBudgetViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addBudgetNavGraph(
    navController: NavHostController
) {
    navigation<AddBudget>(startDestination = AddBudget.Main) {
        composable<AddBudget.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddBudgetViewModel>(navController)
            val category by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            AddBudgetScreen(
                category = category,
                result = addResult,
                onNavigateBack = navController::navigateUp,
                onNavigateToCategory = { navController.navigate(AddBudget.Category) },
                onAddNewBudget = viewModel::createNewBudgeting
            )
        }

        composable<AddBudget.Category>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddBudgetViewModel>(navController)

            AddBudgetCategoryScreen(
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::changeTransactionCategory
            )
        }
    }
}

fun NavGraphBuilder.budgetDetailNavGraph(
    navController: NavHostController
) {
    navigation<BudgetDetail>(startDestination = BudgetDetail.Main()) {
        composable<BudgetDetail.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<BudgetDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val budgetState by viewModel.budgetState.collectAsStateWithLifecycle()
            val transactions by viewModel.transactions.collectAsStateWithLifecycle()

            budgetState?.let { budgetState ->
                val budgetActions = object : BudgetDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditBudget(budgetId: Long) {
                        navController.navigate(BudgetDetail.Edit(budgetId))
                    }

                    override fun onRemoveBudget(budgetId: Long) {
                        viewModel.deleteBudget(budgetId)
                    }

                    override fun onNavigateToTransactionDetail(transactionId: Long) {
                        navController.navigate(TransactionDetail.Main(transactionId))
                    }
                }

                BudgetDetailScreen(
                    budgetState = budgetState,
                    transactions = transactions,
                    budgetActions = budgetActions
                )
            }
        }

        composable<BudgetDetail.Edit>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<EditBudgetViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val budgetState by viewModel.budgetState.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            budgetState?.let { budgetState ->
                EditBudgetScreen(
                    budgetState = budgetState,
                    result = editResult,
                    onNavigateBack = navController::navigateUp,
                    onEditBudget = viewModel::updateBudget
                )
            }
        }
    }
}

fun NavGraphBuilder.inactiveBudgetNavGraph(
    navController: NavHostController
) {
    navigation<InactiveBudget>(startDestination = InactiveBudget.Main) {
        composable<InactiveBudget.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<InactiveBudgetViewModel>()
            val budgets = viewModel.budgets.collectAsLazyPagingItems()

            InactiveBudgetScreen(
                budgets = budgets,
                onNavigateBack = navController::navigateUp,
                onNavigateToBudgetDetail = { budgetId ->
                    navController.navigate(BudgetDetail.Main(budgetId))
                }
            )
        }
    }
}