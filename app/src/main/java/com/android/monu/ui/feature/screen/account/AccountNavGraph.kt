package com.android.monu.ui.feature.screen.account

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.android.monu.ui.feature.screen.account.accountDetail.AccountDetailActions
import com.android.monu.ui.feature.screen.account.accountDetail.AccountDetailScreen
import com.android.monu.ui.feature.screen.account.accountDetail.AccountDetailViewModel
import com.android.monu.ui.feature.screen.account.addAccount.AddAccountActions
import com.android.monu.ui.feature.screen.account.addAccount.AddAccountScreen
import com.android.monu.ui.feature.screen.account.addAccount.AddAccountViewModel
import com.android.monu.ui.feature.screen.account.addAccount.components.AddAccountContentState
import com.android.monu.ui.feature.screen.account.components.AccountTypeScreen
import com.android.monu.ui.feature.screen.account.editAccount.EditAccountActions
import com.android.monu.ui.feature.screen.account.editAccount.EditAccountScreen
import com.android.monu.ui.feature.screen.account.editAccount.EditAccountViewModel
import com.android.monu.ui.feature.screen.account.editAccount.components.EditAccountContentState
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.Account
import com.android.monu.ui.navigation.AccountDetail
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
            val account by viewModel.account.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            account?.let { account ->
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
                    account = account,
                    editResult = editResult,
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
            val account by viewModel.account.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            account?.let { account ->
                val editAccountActions = object : EditAccountActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                        viewModel.restoreOriginalAccount()
                    }

                    override fun onNavigateToType() {
                        navController.navigate(AccountDetail.Type)
                    }

                    override fun onEditAccount(accountState: EditAccountContentState) {
                        viewModel.updateAccount(accountState)
                    }
                }

                EditAccountScreen(
                    account = account,
                    editResult = editResult,
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