package com.android.monu.ui.feature.screen.saving

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.monu.domain.model.DepositWithdrawState
import com.android.monu.domain.model.SavingState
import com.android.monu.ui.feature.screen.saving.addSaving.AddSavingScreen
import com.android.monu.ui.feature.screen.saving.addSaving.AddSavingViewModel
import com.android.monu.ui.feature.screen.saving.components.SavingAccountScreen
import com.android.monu.ui.feature.screen.saving.deposit.DepositActions
import com.android.monu.ui.feature.screen.saving.deposit.DepositScreen
import com.android.monu.ui.feature.screen.saving.deposit.DepositUiState
import com.android.monu.ui.feature.screen.saving.deposit.DepositViewModel
import com.android.monu.ui.feature.screen.saving.editSaving.EditSavingScreen
import com.android.monu.ui.feature.screen.saving.editSaving.EditSavingUiState
import com.android.monu.ui.feature.screen.saving.editSaving.EditSavingViewModel
import com.android.monu.ui.feature.screen.saving.inactiveSaving.InactiveSavingScreen
import com.android.monu.ui.feature.screen.saving.inactiveSaving.InactiveSavingViewModel
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailActions
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailScreen
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailState
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailViewModel
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawActions
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawScreen
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawUiState
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawViewModel
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.Account
import com.android.monu.ui.navigation.Deposit
import com.android.monu.ui.navigation.TransactionDetail
import com.android.monu.ui.navigation.Withdraw
import com.android.monu.utils.TransactionChildCategory
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import com.android.monu.ui.navigation.Saving as SavingRoute

fun NavGraphBuilder.savingNavGraph(
    navController: NavHostController
) {
    navigation<SavingRoute>(startDestination = SavingRoute.Main) {
        composable<SavingRoute.Main>(
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
                    navController.navigate(SavingRoute.Inactive)
                }

                override fun onNavigateToAddSaving() {
                    navController.navigate(SavingRoute.Add)
                }

                override fun onNavigateToSavingDetail(savingId: Long) {
                    navController.navigate(SavingRoute.Detail(savingId))
                }
            }

            SavingScreen(
                totalCurrentAmount = totalCurrentAmount ?: 0,
                savings = savings,
                savingActions = savingActions
            )
        }
        composable<SavingRoute.Add>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AddSavingViewModel>()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            AddSavingScreen(
                addResult = addResult,
                onNavigateBack = navController::navigateUp,
                onAddNewSaving = viewModel::createNewSaving
            )
        }
        composable<SavingRoute.Detail>(
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
                val savingDetailState = SavingDetailState(
                    savingState = saving,
                    transactions = transactions,
                    removeProgress = removeProgress,
                    completeResult = completeResult
                )

                val savingDetailActions = object : SavingDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditSaving(savingId: Long) {
                        navController.navigate(SavingRoute.Edit(savingId))
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
                    savingState = savingDetailState,
                    savingActions = savingDetailActions
                )
            }
        }
        composable<SavingRoute.Edit>(
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
                val editSavingUiState = EditSavingUiState(
                    id = savingState.id,
                    title = savingState.title,
                    targetDate = savingState.targetDate,
                    currentAmount = savingState.currentAmount,
                    targetAmount = savingState.targetAmount,
                    editResult = editResult
                )

                EditSavingScreen(
                    savingUiState = editSavingUiState,
                    onNavigateBack = navController::navigateUp,
                    onEditSaving = viewModel::updateSaving
                )
            }
        }
        composable<SavingRoute.Inactive>(
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
                    navController.navigate(SavingRoute.Detail(savingId))
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

            val depositUiState = DepositUiState(
                account = account,
                saving = saving,
                addResult = addResult
            )

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
                depositUiState = depositUiState,
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

            SavingAccountScreen(
                category = TransactionChildCategory.SAVINGS_IN,
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

            val withdrawUiState = WithdrawUiState(
                account = account,
                saving = saving,
                addResult = addResult
            )

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
                withdrawUiState = withdrawUiState,
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

            SavingAccountScreen(
                category = TransactionChildCategory.SAVINGS_OUT,
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::changeTransactionDestination,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }
    }
}