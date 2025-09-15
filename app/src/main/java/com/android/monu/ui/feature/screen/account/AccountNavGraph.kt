package com.android.monu.ui.feature.screen.account

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.ui.feature.screen.account.addAccount.AddAccountActions
import com.android.monu.ui.feature.screen.account.addAccount.AddAccountScreen
import com.android.monu.ui.feature.screen.account.addAccount.AddAccountViewModel
import com.android.monu.ui.feature.screen.account.addAccount.components.AddAccountContentState
import com.android.monu.ui.feature.screen.account.addAccount.components.AddAccountTypeScreen
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.Account
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.accountNavGraph(
    navController: NavHostController
) {
    navigation<Account>(startDestination = Account.Main) {
        composable<Account.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AccountViewModel>()
            val totalBalance by viewModel.totalAccountBalance.collectAsStateWithLifecycle()
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AccountScreen(
                accounts = accounts,
                totalBalance = totalBalance,
                onNavigateBack = navController::navigateUp,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }
        composable<Account.Add>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddAccountViewModel>(navController)
            val accountType by viewModel.accountType.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val addAccountActions = object : AddAccountActions {
                override fun onNavigateBack() {
                    viewModel.changeAccountType(0)
                    navController.navigateUp()
                }

                override fun onNavigateToType() {
                    navController.navigate(Account.Type)
                }

                override fun onAddNewAccount(accountState: AddAccountContentState) {
                    viewModel.createNewAccountWithInitialTransaction(accountState)
                }
            }

            AddAccountScreen(
                accountType = accountType,
                addResult = addResult,
                accountActions = addAccountActions
            )
        }
        composable<Account.Type>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddAccountViewModel>(navController)

            AddAccountTypeScreen(
                onNavigateBack = navController::navigateUp,
                onTypeSelect = viewModel::changeAccountType
            )
        }
    }
}