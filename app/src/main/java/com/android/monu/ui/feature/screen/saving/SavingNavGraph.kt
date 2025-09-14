package com.android.monu.ui.feature.screen.saving

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.monu.ui.feature.screen.saving.addsave.AddSaveScreen
import com.android.monu.ui.feature.screen.saving.addsave.AddSaveViewModel
import com.android.monu.ui.feature.screen.saving.components.SaveAccountScreen
import com.android.monu.ui.feature.screen.saving.deposit.DepositActions
import com.android.monu.ui.feature.screen.saving.deposit.DepositScreen
import com.android.monu.ui.feature.screen.saving.deposit.DepositState
import com.android.monu.ui.feature.screen.saving.deposit.DepositViewModel
import com.android.monu.ui.feature.screen.saving.deposit.components.DepositContentState
import com.android.monu.ui.feature.screen.saving.editsave.EditSaveScreen
import com.android.monu.ui.feature.screen.saving.editsave.EditSaveState
import com.android.monu.ui.feature.screen.saving.editsave.EditSaveViewModel
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailActions
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailScreen
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailViewModel
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
import com.android.monu.ui.navigation.Saving
import com.android.monu.ui.navigation.Withdraw
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
            val saves by viewModel.saves.collectAsStateWithLifecycle()

            val saveActions = object : SaveActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToAddSave() {
                    navController.navigate(Saving.Add)
                }

                override fun onNavigateToSaveDetail(saveId: Long) {
                    navController.navigate(Saving.Detail(saveId))
                }
            }

            SavingScreen(
                totalCurrentAmount = totalCurrentAmount ?: 0,
                saves = saves,
                saveActions = saveActions
            )
        }
        composable<Saving.Add>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AddSaveViewModel>()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            AddSaveScreen(
                addResult = addResult,
                onNavigateBack = navController::navigateUp,
                onAddNewSave = viewModel::createNewSave
            )
        }
        composable<Saving.Detail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<SaveDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val save by viewModel.save.collectAsStateWithLifecycle()
            val transactions by viewModel.transactions.collectAsStateWithLifecycle()
            val removeProgress by viewModel.deleteProgress.collectAsStateWithLifecycle()

            save?.let { save ->
                val saveDetailActions = object : SaveDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditSave(saveId: Long) {
                        navController.navigate(Saving.Edit(saveId))
                    }

                    override fun onRemoveSave(saveId: Long) {
                        viewModel.deleteSave(saveId)
                    }

                    override fun onNavigateToDeposit() {
                        navController.navigate(Deposit.Main(save.id, save.title))
                    }

                    override fun onNavigateToWithdraw() {
                        navController.navigate(Withdraw.Main(save.id, save.title))
                    }

                    override fun onNavigateToTransactionDetail(transactionId: Long) {
                        navController.navigate(MainTransactionDetail(transactionId))
                    }
                }

                SaveDetailScreen(
                    save = save,
                    transactions = transactions,
                    removeProgress = removeProgress,
                    saveActions = saveDetailActions
                )
            }
        }
        composable<Saving.Edit>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<EditSaveViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val save by viewModel.save.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            save?.let { save ->
                val editSaveState = EditSaveState(
                    id = save.id,
                    title = save.title,
                    targetDate = save.targetDate,
                    currentAmount = save.currentAmount,
                    targetAmount = save.targetAmount,
                    editResult = editResult
                )

                EditSaveScreen(
                    saveState = editSaveState,
                    onNavigateBack = navController::navigateUp,
                    onEditSave = viewModel::updateSave
                )
            }
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
            val save = Pair(args.saveId ?: 0, args.saveName ?: "")

            val depositState = DepositState(
                account = account,
                save = save,
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

            SaveAccountScreen(
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
            val save = Pair(args.saveId ?: 0, args.saveName ?: "")

            val withdrawState = WithdrawState(
                account = account,
                save = save,
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

            SaveAccountScreen(
                category = TransactionChildCategory.SAVINGS_OUT,
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::changeTransactionDestination
            )
        }
    }
}