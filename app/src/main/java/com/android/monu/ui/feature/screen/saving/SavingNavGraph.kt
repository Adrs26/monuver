package com.android.monu.ui.feature.screen.saving

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.monu.domain.model.saving.Saving
import com.android.monu.ui.feature.screen.saving.addSaving.AddSavingScreen
import com.android.monu.ui.feature.screen.saving.addSaving.AddSavingViewModel
import com.android.monu.ui.feature.screen.saving.components.SavingAccountScreen
import com.android.monu.ui.feature.screen.saving.deposit.DepositActions
import com.android.monu.ui.feature.screen.saving.deposit.DepositScreen
import com.android.monu.ui.feature.screen.saving.deposit.DepositState
import com.android.monu.ui.feature.screen.saving.deposit.DepositViewModel
import com.android.monu.ui.feature.screen.saving.deposit.components.DepositContentState
import com.android.monu.ui.feature.screen.saving.editSaving.EditSavingScreen
import com.android.monu.ui.feature.screen.saving.editSaving.EditSavingState
import com.android.monu.ui.feature.screen.saving.editSaving.EditSavingViewModel
import com.android.monu.ui.feature.screen.saving.inactiveSaving.InactiveSavingScreen
import com.android.monu.ui.feature.screen.saving.inactiveSaving.InactiveSavingViewModel
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailActions
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailScreen
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailState
import com.android.monu.ui.feature.screen.saving.savingDetail.SavingDetailViewModel
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawActions
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawScreen
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawState
import com.android.monu.ui.feature.screen.saving.withdraw.WithdrawViewModel
import com.android.monu.ui.feature.screen.saving.withdraw.components.WithdrawContentState
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.TransactionChildCategory
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.Deposit
import com.android.monu.ui.navigation.MainTransactionDetail
import com.android.monu.ui.navigation.Withdraw
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
                    saving = saving,
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
                        navController.navigate(MainTransactionDetail(transactionId))
                    }

                    override fun onCompleteSaving(saving: Saving) {
                        viewModel.completeSaving(saving)
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
            val saving by viewModel.saving.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            saving?.let { saving ->
                val editSavingState = EditSavingState(
                    id = saving.id,
                    title = saving.title,
                    targetDate = saving.targetDate,
                    currentAmount = saving.currentAmount,
                    targetAmount = saving.targetAmount,
                    editResult = editResult
                )

                EditSavingScreen(
                    savingState = editSavingState,
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

            val depositState = DepositState(
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

                override fun onAddNewDeposit(depositState: DepositContentState) {
                    viewModel.createNewTransaction(depositState)
                }
            }

            DepositScreen(
                depositState = depositState,
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
                onAccountSelect = viewModel::changeTransactionSource
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

            val withdrawState = WithdrawState(
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

                override fun onAddNewWithdraw(withdrawState: WithdrawContentState) {
                    viewModel.createNewTransaction(withdrawState)
                }
            }

            WithdrawScreen(
                withdrawState = withdrawState,
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
                onAccountSelect = viewModel::changeTransactionDestination
            )
        }
    }
}