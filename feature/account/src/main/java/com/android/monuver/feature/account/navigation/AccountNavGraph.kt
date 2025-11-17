package com.android.monuver.feature.account.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monuver.core.presentation.navigation.Account
import com.android.monuver.core.presentation.navigation.AccountDetail
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.core.presentation.util.sharedKoinViewModel
import com.android.monuver.feature.account.domain.model.EditAccountState
import com.android.monuver.feature.account.presentation.AccountActions
import com.android.monuver.feature.account.presentation.AccountScreen
import com.android.monuver.feature.account.presentation.AccountViewModel
import com.android.monuver.feature.account.presentation.accountDetail.AccountDetailActions
import com.android.monuver.feature.account.presentation.accountDetail.AccountDetailScreen
import com.android.monuver.feature.account.presentation.accountDetail.AccountDetailViewModel
import com.android.monuver.feature.account.presentation.addAccount.AddAccountScreen
import com.android.monuver.feature.account.presentation.addAccount.AddAccountViewModel
import com.android.monuver.feature.account.presentation.components.AccountTypeScreen
import com.android.monuver.feature.account.presentation.editAccount.EditAccountActions
import com.android.monuver.feature.account.presentation.editAccount.EditAccountScreen
import com.android.monuver.feature.account.presentation.editAccount.EditAccountViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

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

            val accountActions = object : AccountActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToAccountDetail(accountId: Int) {
                    navController.navigate(AccountDetail.Main(accountId))
                }

                override fun onNavigateToAddAccount() {
                    navController.navigate(Account.Add)
                }
            }

            AccountScreen(
                accounts = accounts,
                totalBalance = totalBalance,
                accountActions = accountActions
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

            AddAccountScreen(
                accountType = accountType,
                result = addResult,
                onNavigateBack = {
                    viewModel.changeAccountType(0)
                    navController.navigateUp()
                },
                onNavigateToType = { navController.navigate(Account.Type) },
                onAddAccount = viewModel::createNewAccount
            )
        }
        composable<Account.Type>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddAccountViewModel>(navController)

            AccountTypeScreen(
                onNavigateBack = navController::navigateUp,
                onTypeSelect = viewModel::changeAccountType
            )
        }
    }
}

fun NavGraphBuilder.accountDetailNavGraph(
    navController: NavHostController
) {
    navigation<AccountDetail>(startDestination = AccountDetail.Main()) {
        composable<AccountDetail.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AccountDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val accountState by viewModel.accountState.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            accountState?.let { accountState ->
                val accountDetailActions = object : AccountDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditAccount(accountId: Int) {
                        navController.navigate(AccountDetail.Edit(accountId))
                    }

                    override fun onDeactivateAccount(accountId: Int) {
                        viewModel.updateAccountStatus(accountId, false)
                    }

                    override fun onActivateAccount(accountId: Int) {
                        viewModel.updateAccountStatus(accountId, true)
                    }
                }

                AccountDetailScreen(
                    accountState = accountState,
                    result = editResult,
                    accountActions = accountDetailActions
                )
            }
        }
        composable<AccountDetail.Edit>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<EditAccountViewModel>(navController) {
                parametersOf(it.savedStateHandle)
            }
            val accountState by viewModel.accountState.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            accountState?.let { accountState ->
                val editAccountActions = object : EditAccountActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                        viewModel.restoreOriginalAccount()
                    }

                    override fun onNavigateToType() {
                        navController.navigate(AccountDetail.Type)
                    }

                    override fun onEditAccount(accountState: EditAccountState) {
                        viewModel.updateAccount(accountState)
                    }
                }

                EditAccountScreen(
                    accountState = accountState,
                    result = editResult,
                    accountActions = editAccountActions
                )
            }
        }
        composable<AccountDetail.Type>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<EditAccountViewModel>(navController)

            AccountTypeScreen(
                onNavigateBack = navController::navigateUp,
                onTypeSelect = viewModel::changeAccountType
            )
        }
    }
}