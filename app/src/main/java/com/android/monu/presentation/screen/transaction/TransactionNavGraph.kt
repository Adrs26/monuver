package com.android.monu.presentation.screen.transaction

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.presentation.screen.transaction.addtransaction.AddTransactionActions
import com.android.monu.presentation.screen.transaction.addtransaction.AddTransactionScreen
import com.android.monu.presentation.screen.transaction.addtransaction.AddTransactionState
import com.android.monu.presentation.screen.transaction.addtransaction.AddTransactionViewModel
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionContentState
import com.android.monu.presentation.screen.transaction.addtransaction.components.TransactionCategoryScreen
import com.android.monu.presentation.screen.transaction.addtransaction.components.TransactionSourceScreen
import com.android.monu.presentation.screen.transaction.detailtransaction.DetailTransactionActions
import com.android.monu.presentation.screen.transaction.detailtransaction.DetailTransactionScreen
import com.android.monu.presentation.screen.transaction.detailtransaction.DetailTransactionViewModel
import com.android.monu.presentation.screen.transaction.transfer.TransferActions
import com.android.monu.presentation.screen.transaction.transfer.TransferScreen
import com.android.monu.presentation.screen.transaction.transfer.TransferState
import com.android.monu.presentation.screen.transaction.transfer.TransferViewModel
import com.android.monu.presentation.screen.transaction.transfer.components.TransferAccountScreen
import com.android.monu.presentation.screen.transaction.transfer.components.TransferContentState
import com.android.monu.ui.navigation.AddTransaction
import com.android.monu.ui.navigation.DetailTransaction
import com.android.monu.ui.navigation.MainAddTransaction
import com.android.monu.ui.navigation.MainDetailTransaction
import com.android.monu.ui.navigation.MainTransfer
import com.android.monu.ui.navigation.TransactionCategory
import com.android.monu.ui.navigation.TransactionSource
import com.android.monu.ui.navigation.Transfer
import com.android.monu.ui.navigation.TransferAccount
import com.android.monu.presentation.utils.NavigationAnimation
import com.android.monu.presentation.utils.SelectAccountType
import com.android.monu.presentation.utils.sharedKoinViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addTransactionNavGraph(
    navController: NavHostController
) {
    navigation<AddTransaction>(startDestination = MainAddTransaction()) {
        composable<MainAddTransaction>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<MainAddTransaction>()
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)
            val category by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val source by viewModel.transactionSource.collectAsStateWithLifecycle()
            val addTransactionResult by viewModel.createTransactionResult.collectAsStateWithLifecycle()

            val addTransactionState = AddTransactionState(
                type = args.type,
                category = category,
                source = source,
                addTransactionResult = addTransactionResult
            )

            val addTransactionActions = object : AddTransactionActions {
                override fun onNavigateBack() {
                    viewModel.changeTransactionCategory(0, 0)
                    viewModel.changeTransactionSource(0, "")
                    navController.navigateUp()
                }

                override fun onNavigateToCategory(transactionType: Int) {
                    navController.navigate(TransactionCategory(args.type))
                }

                override fun onNavigateToSource() {
                    navController.navigate(TransactionSource)
                }

                override fun onAddNewTransaction(
                    transactionState: AddTransactionContentState
                ) {
                    viewModel.createNewTransaction(transactionState)
                }
            }

            AddTransactionScreen(
                transactionState = addTransactionState,
                transactionActions = addTransactionActions
            )
        }
        composable<TransactionCategory>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<TransactionCategory>()
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = args.type,
                onCategorySelect = viewModel::changeTransactionCategory,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable<TransactionSource>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            TransactionSourceScreen(
                accounts = accounts,
                onNavigateBack = { navController.navigateUp() },
                onSourceSelect = viewModel::changeTransactionSource
            )
        }
    }
}

fun NavGraphBuilder.transferNavGraph(
    navController: NavHostController
) {
    navigation<Transfer>(startDestination = MainTransfer) {
        composable<MainTransfer>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<TransferViewModel>(navController)
            val source by viewModel.sourceAccount.collectAsStateWithLifecycle()
            val destination by viewModel.destinationAccount.collectAsStateWithLifecycle()
            val addTransferResult by viewModel.createTransferResult.collectAsStateWithLifecycle()

            val transferState = TransferState(
                source = source,
                destination = destination,
                addTransferResult = addTransferResult
            )

            val transferActions = object : TransferActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToSourceAccount() {
                    navController.navigate(TransferAccount(SelectAccountType.SOURCE))
                }

                override fun onNavigateToDestinationAccount() {
                    navController.navigate(TransferAccount(SelectAccountType.DESTINATION))
                }

                override fun onAddNewTransfer(transferState: TransferContentState) {
                    viewModel.createNewTransfer(transferState)
                }
            }

            TransferScreen(
                transferState = transferState,
                transferActions = transferActions
            )
        }
        composable<TransferAccount>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<TransferAccount>()
            val viewModel = it.sharedKoinViewModel<TransferViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()
            val selectedAccount by viewModel.selectedAccounts.collectAsStateWithLifecycle(
                initialValue = emptyList()
            )

            TransferAccountScreen(
                selectAccountType = args.type,
                accounts = accounts,
                selectedAccounts = selectedAccount,
                onNavigateBack = { navController.navigateUp() },
                onAccountSelect = { accountId, accountName ->
                    if (args.type == SelectAccountType.SOURCE) {
                        viewModel.changeSourceAccount(accountId, accountName)
                    } else {
                        viewModel.changeDestinationAccount(accountId, accountName)
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.detailTransactionNavGraph(
    navController: NavHostController
) {
    navigation<DetailTransaction>(startDestination = MainDetailTransaction()) {
        composable<MainDetailTransaction>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<DetailTransactionViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val transaction by viewModel.transaction.collectAsStateWithLifecycle()
            val removeTransactionResult by viewModel.deleteTransactionResult.collectAsStateWithLifecycle()

            val detailTransactionActions = object : DetailTransactionActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToEdit() {

                }

                override fun onRemoveTransaction(transaction: Transaction) {
                    viewModel.deleteTransaction(transaction)
                }
            }

            transaction?.let {
                DetailTransactionScreen(
                    transaction = transaction!!,
                    removeTransactionResult = removeTransactionResult,
                    transactionActions = detailTransactionActions
                )
            }
        }
    }
}