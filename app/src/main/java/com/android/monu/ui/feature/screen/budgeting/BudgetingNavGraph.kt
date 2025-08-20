package com.android.monu.ui.feature.screen.budgeting

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.ui.feature.screen.budgeting.addbudget.AddBudgetActions
import com.android.monu.ui.feature.screen.budgeting.addbudget.AddBudgetScreen
import com.android.monu.ui.feature.screen.budgeting.addbudget.AddBudgetViewModel
import com.android.monu.ui.feature.screen.budgeting.addbudget.components.AddBudgetCategoryScreen
import com.android.monu.ui.feature.screen.budgeting.addbudget.components.AddBudgetContentState
import com.android.monu.ui.feature.screen.budgeting.budgetdetail.BudgetDetailActions
import com.android.monu.ui.feature.screen.budgeting.budgetdetail.BudgetDetailScreen
import com.android.monu.ui.feature.screen.budgeting.budgetdetail.BudgetDetailState
import com.android.monu.ui.feature.screen.budgeting.budgetdetail.BudgetDetailViewModel
import com.android.monu.ui.feature.screen.budgeting.editbudget.EditBudgetScreen
import com.android.monu.ui.feature.screen.budgeting.editbudget.EditBudgetState
import com.android.monu.ui.feature.screen.budgeting.editbudget.EditBudgetViewModel
import com.android.monu.ui.feature.screen.budgeting.inactivebudget.InactiveBudgetScreen
import com.android.monu.ui.feature.screen.budgeting.inactivebudget.InactiveBudgetViewModel
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddBudget
import com.android.monu.ui.navigation.AddBudgetCategory
import com.android.monu.ui.navigation.BudgetDetail
import com.android.monu.ui.navigation.EditBudget
import com.android.monu.ui.navigation.InactiveBudget
import com.android.monu.ui.navigation.MainAddBudget
import com.android.monu.ui.navigation.MainBudgetDetail
import com.android.monu.ui.navigation.MainInactiveBudget
import com.android.monu.ui.navigation.MainTransactionDetail
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addBudgetNavGraph(
    navController: NavHostController
) {
    navigation<AddBudget>(startDestination = MainAddBudget) {
        composable<MainAddBudget>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddBudgetViewModel>(navController)
            val category by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val addBudgetActions = object : AddBudgetActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToCategory() {
                    navController.navigate(AddBudgetCategory)
                }

                override fun onAddNewBudget(budgetState: AddBudgetContentState) {
                    viewModel.createNewBudgeting(budgetState)
                }
            }

            AddBudgetScreen(
                category = category,
                addResult = addResult,
                budgetActions = addBudgetActions
            )
        }

        composable<AddBudgetCategory>(
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
    navigation<BudgetDetail>(startDestination = MainBudgetDetail()) {
        composable<MainBudgetDetail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<BudgetDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val budget by viewModel.budget.collectAsStateWithLifecycle()
            val transactions by viewModel.transactions.collectAsStateWithLifecycle()

            budget?.let { budget ->
                val budgetState = BudgetDetailState(
                    id = budget.id,
                    category = budget.category,
                    cycle = budget.cycle,
                    startDate = budget.startDate,
                    endDate = budget.endDate,
                    maxAmount = budget.maxAmount,
                    usedAmount = budget.usedAmount,
                    isActive = budget.isActive
                )

                val budgetActions = object : BudgetDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditBudget(budgetId: Long) {
                        navController.navigate(EditBudget(id = budgetId))
                    }

                    override fun onRemoveBudget(budgetId: Long) {
                        viewModel.deleteBudget(budgetId)
                    }

                    override fun onNavigateToTransactionDetail(transactionId: Long) {
                        navController.navigate(MainTransactionDetail(id = transactionId))
                    }
                }

                BudgetDetailScreen(
                    budgetState = budgetState,
                    transactions = transactions,
                    budgetActions = budgetActions
                )
            }
        }

        composable<EditBudget>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<EditBudgetViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val budget by viewModel.budget.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            budget?.let { budget ->
                val editBudgetState = EditBudgetState(
                    id = budget.id,
                    category = budget.category,
                    maxAmount = budget.maxAmount,
                    cycle = budget.cycle,
                    startDate = budget.startDate,
                    endDate = budget.endDate,
                    isOverflowAllowed = budget.isOverflowAllowed,
                    isAutoUpdate = budget.isAutoUpdate,
                    editResult = editResult
                )

                EditBudgetScreen(
                    budgetState = editBudgetState,
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
    navigation<InactiveBudget>(startDestination = MainInactiveBudget) {
        composable<MainInactiveBudget>(
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
                    navController.navigate(MainBudgetDetail(budgetId))
                }
            )
        }
    }
}