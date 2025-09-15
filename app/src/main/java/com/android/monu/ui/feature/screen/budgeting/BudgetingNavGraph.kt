package com.android.monu.ui.feature.screen.budgeting

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.ui.feature.screen.budgeting.addBudget.AddBudgetActions
import com.android.monu.ui.feature.screen.budgeting.addBudget.AddBudgetScreen
import com.android.monu.ui.feature.screen.budgeting.addBudget.AddBudgetViewModel
import com.android.monu.ui.feature.screen.budgeting.addBudget.components.AddBudgetCategoryScreen
import com.android.monu.ui.feature.screen.budgeting.addBudget.components.AddBudgetContentState
import com.android.monu.ui.feature.screen.budgeting.budgetDetail.BudgetDetailActions
import com.android.monu.ui.feature.screen.budgeting.budgetDetail.BudgetDetailScreen
import com.android.monu.ui.feature.screen.budgeting.budgetDetail.BudgetDetailState
import com.android.monu.ui.feature.screen.budgeting.budgetDetail.BudgetDetailViewModel
import com.android.monu.ui.feature.screen.budgeting.editBudget.EditBudgetScreen
import com.android.monu.ui.feature.screen.budgeting.editBudget.EditBudgetState
import com.android.monu.ui.feature.screen.budgeting.editBudget.EditBudgetViewModel
import com.android.monu.ui.feature.screen.budgeting.inactiveBudget.InactiveBudgetScreen
import com.android.monu.ui.feature.screen.budgeting.inactiveBudget.InactiveBudgetViewModel
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddBudget
import com.android.monu.ui.navigation.BudgetDetail
import com.android.monu.ui.navigation.InactiveBudget
import com.android.monu.ui.navigation.TransactionDetail
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

            val addBudgetActions = object : AddBudgetActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToCategory() {
                    navController.navigate(AddBudget.Category)
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