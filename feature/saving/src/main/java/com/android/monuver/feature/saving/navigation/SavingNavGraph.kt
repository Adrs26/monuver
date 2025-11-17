package com.android.monuver.feature.saving.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.monuver.core.domain.model.SavingState
import com.android.monuver.core.presentation.components.AccountListScreen
import com.android.monuver.core.presentation.navigation.Account
import com.android.monuver.core.presentation.navigation.Deposit
import com.android.monuver.core.presentation.navigation.Saving
import com.android.monuver.core.presentation.navigation.TransactionDetail
import com.android.monuver.core.presentation.navigation.Withdraw
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.core.presentation.util.sharedKoinViewModel
import com.android.monuver.feature.saving.domain.model.DepositWithdrawState
import com.android.monuver.feature.saving.presentation.SavingActions
import com.android.monuver.feature.saving.presentation.SavingScreen
import com.android.monuver.feature.saving.presentation.SavingViewModel
import com.android.monuver.feature.saving.presentation.addSaving.AddSavingScreen
import com.android.monuver.feature.saving.presentation.addSaving.AddSavingViewModel
import com.android.monuver.feature.saving.presentation.deposit.DepositActions
import com.android.monuver.feature.saving.presentation.deposit.DepositScreen
import com.android.monuver.feature.saving.presentation.deposit.DepositViewModel
import com.android.monuver.feature.saving.presentation.editSaving.EditSavingScreen
import com.android.monuver.feature.saving.presentation.editSaving.EditSavingViewModel
import com.android.monuver.feature.saving.presentation.inactiveSaving.InactiveSavingScreen
import com.android.monuver.feature.saving.presentation.inactiveSaving.InactiveSavingViewModel
import com.android.monuver.feature.saving.presentation.savingDetail.SavingDetailActions
import com.android.monuver.feature.saving.presentation.savingDetail.SavingDetailScreen
import com.android.monuver.feature.saving.presentation.savingDetail.SavingDetailViewModel
import com.android.monuver.feature.saving.presentation.withdraw.WithdrawActions
import com.android.monuver.feature.saving.presentation.withdraw.WithdrawScreen
import com.android.monuver.feature.saving.presentation.withdraw.WithdrawViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.savingNavGraph(
    navController: NavHostController
) {
    navigation<Saving>(startDestination = Saving.Main) {
        composable<Saving.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<SavingViewModel>()
            val totalCurrentAmount by viewModel.totalCurrentAmount.collectAsStateWithLifecycle()
            val savings by viewModel.savings.collectAsStateWithLifecycle()

            val savingActions = object : SavingActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToInactiveSaving() {
                    navController.navigate(Saving.Inactive)
                }

                override fun onNavigateToAddSaving() {
                    navController.navigate(Saving.Add)
                }

                override fun onNavigateToSavingDetail(savingId: Long) {
                    navController.navigate(Saving.Detail(savingId))
                }
            }

            SavingScreen(
                totalCurrentAmount = totalCurrentAmount ?: 0,
                savings = savings,
                savingActions = savingActions
            )
        }
        composable<Saving.Add>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AddSavingViewModel>()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            AddSavingScreen(
                result = addResult,
                onNavigateBack = navController::navigateUp,
                onAddNewSaving = viewModel::createNewSaving
            )
        }
        composable<Saving.Detail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<SavingDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val saving by viewModel.saving.collectAsStateWithLifecycle()
            val transactions by viewModel.transactions.collectAsStateWithLifecycle()
            val removeProgress by viewModel.deleteProgress.collectAsStateWithLifecycle()
            val completeResult by viewModel.completeResult.collectAsStateWithLifecycle()

            saving?.let { saving ->
                val savingDetailActions = object : SavingDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditSaving(savingId: Long) {
                        navController.navigate(Saving.Edit(savingId))
                    }

                    override fun onRemoveSaving(savingId: Long) {
                        viewModel.deleteSaving(savingId)
                    }

                    override fun onNavigateToDeposit() {
                        navController.navigate(Deposit.Main(saving.id, saving.title))
                    }

                    override fun onNavigateToWithdraw() {
                        navController.navigate(Withdraw.Main(saving.id, saving.title))
                    }

                    override fun onNavigateToTransactionDetail(transactionId: Long) {
                        navController.navigate(TransactionDetail.Main(transactionId))
                    }

                    override fun onCompleteSaving(savingState: SavingState) {
                        viewModel.completeSaving(savingState)
                    }
                }

                SavingDetailScreen(
                    savingState = saving,
                    transactions = transactions,
                    progress = removeProgress,
                    result = completeResult,
                    savingActions = savingDetailActions
                )
            }
        }
        composable<Saving.Edit>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<EditSavingViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val savingState by viewModel.savingState.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            savingState?.let { savingState ->
                EditSavingScreen(
                    savingState = savingState,
                    result = editResult,
                    onNavigateBack = navController::navigateUp,
                    onEditSaving = viewModel::updateSaving
                )
            }
        }
        composable<Saving.Inactive>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<InactiveSavingViewModel>()
            val savings by viewModel.savings.collectAsStateWithLifecycle()

            InactiveSavingScreen(
                savings = savings,
                onNavigateBack = navController::navigateUp,
                onNavigateToSavingDetail = { savingId ->
                    navController.navigate(Saving.Detail(savingId))
                }
            )
        }
    }
}

fun NavGraphBuilder.depositNavGraph(
    navController: NavHostController
) {
    navigation<Deposit>(startDestination = Deposit.Main()) {
        composable<Deposit.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<Deposit.Main>()
            val viewModel = it.sharedKoinViewModel<DepositViewModel>(navController)
            val account by viewModel.transactionSource.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()
            val saving = Pair(args.savingId ?: 0, args.savingName ?: "")

            val depositActions = object : DepositActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToAccount() {
                    navController.navigate(Deposit.Account)
                }

                override fun onAddNewDeposit(depositState: DepositWithdrawState) {
                    viewModel.createNewTransaction(depositState)
                }
            }

            DepositScreen(
                account = account,
                saving = saving,
                result = addResult,
                depositActions = depositActions
            )
        }
        composable<Deposit.Account>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<DepositViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AccountListScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::changeTransactionSource,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }
    }
}

fun NavGraphBuilder.withdrawNavGraph(
    navController: NavHostController
) {
    navigation<Withdraw>(startDestination = Withdraw.Main()) {
        composable<Withdraw.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<Withdraw.Main>()
            val viewModel = it.sharedKoinViewModel<WithdrawViewModel>(navController)
            val account by viewModel.transactionDestination.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()
            val saving = Pair(args.savingId ?: 0, args.savingName ?: "")

            val withdrawActions = object : WithdrawActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToAccount() {
                    navController.navigate(Withdraw.Account)
                }

                override fun onAddNewWithdraw(withdrawState: DepositWithdrawState) {
                    viewModel.createNewTransaction(withdrawState)
                }
            }

            WithdrawScreen(
                account = account,
                saving = saving,
                result = addResult,
                withdrawActions = withdrawActions
            )
        }
        composable<Withdraw.Account>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<WithdrawViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AccountListScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::changeTransactionDestination,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }
    }
}