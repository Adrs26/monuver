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
import com.android.monu.presentation.screen.transaction.addtransaction.components.AddTransactionSourceScreen
import com.android.monu.presentation.screen.transaction.components.TransactionCategoryScreen
import com.android.monu.presentation.screen.transaction.edittransaction.EditTransactionActions
import com.android.monu.presentation.screen.transaction.edittransaction.EditTransactionScreen
import com.android.monu.presentation.screen.transaction.edittransaction.EditTransactionState
import com.android.monu.presentation.screen.transaction.edittransaction.EditTransactionViewModel
import com.android.monu.presentation.screen.transaction.edittransaction.components.EditTransactionContentState
import com.android.monu.presentation.screen.transaction.edittransfer.EditTransferActions
import com.android.monu.presentation.screen.transaction.edittransfer.EditTransferScreen
import com.android.monu.presentation.screen.transaction.edittransfer.EditTransferState
import com.android.monu.presentation.screen.transaction.edittransfer.EditTransferViewModel
import com.android.monu.presentation.screen.transaction.edittransfer.components.EditTransferContentState
import com.android.monu.presentation.screen.transaction.transactiondetail.DetailTransactionScreen
import com.android.monu.presentation.screen.transaction.transactiondetail.TransactionDetailActions
import com.android.monu.presentation.screen.transaction.transactiondetail.TransactionDetailViewModel
import com.android.monu.presentation.screen.transaction.transfer.TransferActions
import com.android.monu.presentation.screen.transaction.transfer.TransferScreen
import com.android.monu.presentation.screen.transaction.transfer.TransferState
import com.android.monu.presentation.screen.transaction.transfer.TransferViewModel
import com.android.monu.presentation.screen.transaction.transfer.components.TransferAccountScreen
import com.android.monu.presentation.screen.transaction.transfer.components.TransferContentState
import com.android.monu.presentation.utils.NavigationAnimation
import com.android.monu.presentation.utils.SelectAccountType
import com.android.monu.presentation.utils.TransactionChildCategory
import com.android.monu.presentation.utils.TransactionType
import com.android.monu.presentation.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddTransaction
import com.android.monu.ui.navigation.AddTransactionCategory
import com.android.monu.ui.navigation.AddTransactionSource
import com.android.monu.ui.navigation.EditTransaction
import com.android.monu.ui.navigation.EditTransactionCategory
import com.android.monu.ui.navigation.EditTransfer
import com.android.monu.ui.navigation.MainAddTransaction
import com.android.monu.ui.navigation.MainTransactionDetail
import com.android.monu.ui.navigation.MainTransfer
import com.android.monu.ui.navigation.TransactionDetail
import com.android.monu.ui.navigation.Transfer
import com.android.monu.ui.navigation.TransferAccount
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
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val addTransactionState = AddTransactionState(
                type = args.type,
                category = category,
                source = source,
                addResult = addResult
            )

            val addTransactionActions = object : AddTransactionActions {
                override fun onNavigateBack() {
                    viewModel.changeTransactionCategory(0, 0)
                    viewModel.changeTransactionSource(0, "")
                    navController.navigateUp()
                }

                override fun onNavigateToCategory(transactionType: Int) {
                    navController.navigate(AddTransactionCategory(args.type))
                }

                override fun onNavigateToSource() {
                    navController.navigate(AddTransactionSource)
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
        composable<AddTransactionCategory>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<AddTransactionCategory>()
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = args.type,
                onCategorySelect = viewModel::changeTransactionCategory,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable<AddTransactionSource>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AddTransactionSourceScreen(
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
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val transferState = TransferState(
                source = source,
                destination = destination,
                addResult = addResult
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

fun NavGraphBuilder.transactionDetailNavGraph(
    navController: NavHostController
) {
    navigation<TransactionDetail>(startDestination = MainTransactionDetail()) {
        composable<MainTransactionDetail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<TransactionDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val transaction by viewModel.transaction.collectAsStateWithLifecycle()

            val detailTransactionActions = object : TransactionDetailActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToEditTransaction(
                    transactionId: Long,
                    transactionType: Int,
                    transactionCategory: Int
                ) {
                    when {
                        transactionType == TransactionType.INCOME ||
                                transactionType == TransactionType.EXPENSE -> {
                            navController.navigate(EditTransaction(transactionId))
                        }
                        transactionType == TransactionType.TRANSFER &&
                                transactionCategory == TransactionChildCategory.TRANSFER_ACCOUNT -> {
                            navController.navigate(EditTransfer(transactionId))
                        }
                    }
                }

                override fun onRemoveTransaction(transaction: Transaction) {
                    viewModel.deleteTransaction(transaction)
                }
            }

            transaction?.let { transaction ->
                DetailTransactionScreen(
                    transaction = transaction,
                    transactionActions = detailTransactionActions
                )
            }
        }
        composable<EditTransaction>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<EditTransactionViewModel>(navController) {
                parametersOf(it.savedStateHandle)
            }
            val transaction by viewModel.transaction.collectAsStateWithLifecycle()
            val initialTransaction by viewModel.initialTransaction.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            transaction?.let { transaction ->
                initialTransaction?.let { initialTransaction ->
                    val editTransactionState = EditTransactionState(
                        id = transaction.id,
                        title = transaction.title,
                        type = transaction.type,
                        parentCategory = transaction.parentCategory,
                        childCategory = transaction.childCategory,
                        date = transaction.date,
                        amount = transaction.amount,
                        sourceId = transaction.sourceId,
                        sourceName = transaction.sourceName,
                        editResult = editResult
                    )

                    val editTransactionActions = object : EditTransactionActions {
                        override fun onNavigateBack() {
                            navController.navigateUp()
                            viewModel.restoreOriginalTransaction()
                        }

                        override fun onNavigateToCategory(transactionType: Int) {
                            navController.navigate(EditTransactionCategory(transactionType))
                        }

                        override fun onEditTransaction(transactionState: EditTransactionContentState) {
                            viewModel.updateTransaction(transactionState)
                        }
                    }

                    EditTransactionScreen(
                        transactionState = editTransactionState,
                        transactionActions = editTransactionActions,
                        initialTransaction = initialTransaction
                    )
                }
            }
        }
        composable<EditTransactionCategory>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<EditTransactionCategory>()
            val viewModel = it.sharedKoinViewModel<EditTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = args.type,
                onCategorySelect = viewModel::changeTransactionCategory,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        composable<EditTransfer>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<EditTransferViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val transaction by viewModel.transaction.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            transaction?.let { transaction ->
                val editTransferState = EditTransferState(
                    id = transaction.id,
                    date = transaction.date,
                    amount = transaction.amount,
                    sourceId = transaction.sourceId,
                    sourceName = transaction.sourceName,
                    destinationId = transaction.destinationId ?: 0,
                    destinationName = transaction.destinationName ?: "",
                    editResult = editResult
                )

                val editTransferActions = object : EditTransferActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onEditTransfer(transferState: EditTransferContentState) {
                        viewModel.updateTransaction(transferState)
                    }
                }

                EditTransferScreen(
                    transferState = editTransferState,
                    transferActions = editTransferActions
                )
            }
        }
    }
}