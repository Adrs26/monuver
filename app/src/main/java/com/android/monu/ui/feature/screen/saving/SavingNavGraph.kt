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
import com.android.monu.ui.feature.screen.saving.deposit.DepositActions
import com.android.monu.ui.feature.screen.saving.deposit.DepositScreen
import com.android.monu.ui.feature.screen.saving.deposit.DepositState
import com.android.monu.ui.feature.screen.saving.deposit.DepositViewModel
import com.android.monu.ui.feature.screen.saving.deposit.components.DepositContentState
import com.android.monu.ui.feature.screen.saving.deposit.components.DepositDestinationScreen
import com.android.monu.ui.feature.screen.saving.deposit.components.DepositSourceScreen
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailScreen
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailViewModel
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddSave
import com.android.monu.ui.navigation.Deposit
import com.android.monu.ui.navigation.DepositDestination
import com.android.monu.ui.navigation.DepositSource
import com.android.monu.ui.navigation.MainDeposit
import com.android.monu.ui.navigation.MainSaving
import com.android.monu.ui.navigation.MainTransactionDetail
import com.android.monu.ui.navigation.SaveDetail
import com.android.monu.ui.navigation.Saving
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.savingNavGraph(
    navController: NavHostController
) {
    navigation<Saving>(startDestination = MainSaving) {
        composable<MainSaving>(
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
                    navController.navigate(AddSave)
                }

                override fun onNavigateToSaveDetail(saveId: Long) {
                    navController.navigate(SaveDetail(saveId))
                }
            }

            SavingScreen(
                totalCurrentAmount = totalCurrentAmount ?: 0,
                saves = saves,
                saveActions = saveActions
            )
        }
        composable<AddSave>(
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
        composable<SaveDetail>(
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

            save?.let { save ->
                SaveDetailScreen(
                    save = save,
                    transactions = transactions,
                    onNavigateBack = navController::navigateUp,
                    onAddAmountClick = { navController.navigate(MainDeposit(save.id, save.title)) },
                    onWithdrawAmountClick = { },
                    onNavigateToTransactionDetail = { transactionId ->
                        navController.navigate(MainTransactionDetail(transactionId))
                    }
                )
            }
        }
    }
}

fun NavGraphBuilder.addSaveAmountNavGraph(
    navController: NavHostController
) {
    navigation<Deposit>(startDestination = MainDeposit()) {
        composable<MainDeposit>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<MainDeposit>()
            val viewModel = it.sharedKoinViewModel<DepositViewModel>(navController)
            val destination by viewModel.transactionDestination.collectAsStateWithLifecycle()
            val source by viewModel.transactionSource.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val depositState = DepositState(
                destination = destination,
                source = source,
                fixDestinationId = args.saveId,
                fixDestinationName = args.saveName,
                addResult = addResult
            )

            val depositActions = object : DepositActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToDestination() {
                    navController.navigate(DepositDestination)
                }

                override fun onNavigateToSource() {
                    navController.navigate(DepositSource)
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
        composable<DepositDestination>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<DepositViewModel>(navController)
            val saves by viewModel.saves.collectAsStateWithLifecycle()

            DepositDestinationScreen(
                saves = saves,
                onNavigateBack = navController::navigateUp,
                onDestinationSelect = viewModel::changeTransactionDestination
            )
        }
        composable<DepositSource>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<DepositViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            DepositSourceScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onSourceSelect = viewModel::changeTransactionSource
            )
        }
    }
}