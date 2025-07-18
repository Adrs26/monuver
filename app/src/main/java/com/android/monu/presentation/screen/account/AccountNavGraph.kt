package com.android.monu.presentation.screen.account

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.presentation.screen.account.addaccount.AddAccountActions
import com.android.monu.presentation.screen.account.addaccount.AddAccountScreen
import com.android.monu.presentation.screen.account.addaccount.AddAccountViewModel
import com.android.monu.presentation.screen.account.addaccount.components.AccountTypeScreen
import com.android.monu.presentation.screen.account.addaccount.components.AddAccountContentState
import com.android.monu.ui.navigation.Account
import com.android.monu.ui.navigation.AccountType
import com.android.monu.ui.navigation.AddAccount
import com.android.monu.ui.navigation.MainAccount
import com.android.monu.utils.NavigationAnimation
import com.android.monu.utils.extensions.sharedKoinViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.accountNavGraph(
    navController: NavHostController
) {
    navigation<Account>(startDestination = MainAccount) {
        composable<MainAccount>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AccountViewModel>()
            val totalAccountBalance by viewModel.totalAccountBalance.collectAsStateWithLifecycle()
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AccountScreen(
                accounts = accounts,
                totalAccountBalance = totalAccountBalance,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToAddAccount = { navController.navigate(route = AddAccount) }
            )
        }
        composable<AddAccount>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddAccountViewModel>(navController)
            val accountType by viewModel.accountType.collectAsStateWithLifecycle()
            val addAccountResult by viewModel.createAccountResult.collectAsStateWithLifecycle()

            val addAccountActions = object : AddAccountActions {
                override fun onNavigateBack() {
                    viewModel.changeAccountType(0)
                    navController.navigateUp()
                }

                override fun onNavigateToType() {
                    navController.navigate(AccountType)
                }

                override fun onAddNewAccountWithInitialTransaction(
                    accountState: AddAccountContentState
                ) {
                    viewModel.createNewAccountWithInitialTransaction(accountState)
                }
            }

            AddAccountScreen(
                accountType = accountType,
                addAccountResult = addAccountResult,
                accountActions = addAccountActions
            )
        }
        composable<AccountType>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddAccountViewModel>(navController)

            AccountTypeScreen(
                onNavigateBack = { navController.navigateUp() },
                onTypeSelect = viewModel::changeAccountType
            )
        }
    }
}