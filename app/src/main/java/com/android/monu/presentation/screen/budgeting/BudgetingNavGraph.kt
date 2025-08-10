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
import com.android.monu.presentation.utils.NavigationAnimation
import com.android.monu.presentation.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddBudgeting
import com.android.monu.ui.navigation.AddBudgetingCategory
import com.android.monu.ui.navigation.MainAddBudgeting

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