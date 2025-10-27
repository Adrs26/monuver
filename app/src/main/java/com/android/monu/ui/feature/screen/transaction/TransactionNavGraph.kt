package com.android.monu.ui.feature.screen.transaction

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.monu.domain.model.AddTransactionState
import com.android.monu.domain.model.EditTransactionState
import com.android.monu.domain.model.TransactionState
import com.android.monu.domain.model.TransferState
import com.android.monu.ui.feature.screen.transaction.addTransaction.AddTransactionActions
import com.android.monu.ui.feature.screen.transaction.addTransaction.AddTransactionScreen
import com.android.monu.ui.feature.screen.transaction.addTransaction.AddTransactionUiState
import com.android.monu.ui.feature.screen.transaction.addTransaction.AddTransactionViewModel
import com.android.monu.ui.feature.screen.transaction.addTransaction.components.AddTransactionSourceScreen
import com.android.monu.ui.feature.screen.transaction.components.TransactionCategoryScreen
import com.android.monu.ui.feature.screen.transaction.editTransaction.EditTransactionActions
import com.android.monu.ui.feature.screen.transaction.editTransaction.EditTransactionScreen
import com.android.monu.ui.feature.screen.transaction.editTransaction.EditTransactionUiState
import com.android.monu.ui.feature.screen.transaction.editTransaction.EditTransactionViewModel
import com.android.monu.ui.feature.screen.transaction.transactionDetail.DetailTransactionScreen
import com.android.monu.ui.feature.screen.transaction.transactionDetail.TransactionDetailActions
import com.android.monu.ui.feature.screen.transaction.transactionDetail.TransactionDetailViewModel
import com.android.monu.ui.feature.screen.transaction.transfer.TransferActions
import com.android.monu.ui.feature.screen.transaction.transfer.TransferScreen
import com.android.monu.ui.feature.screen.transaction.transfer.TransferUiState
import com.android.monu.ui.feature.screen.transaction.transfer.TransferViewModel
import com.android.monu.ui.feature.screen.transaction.transfer.components.TransferAccountScreen
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddTransaction
import com.android.monu.ui.navigation.TransactionDetail
import com.android.monu.ui.navigation.Transfer
import com.android.monu.utils.SelectAccountType
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.addTransactionNavGraph(
    navController: NavHostController
) {
    navigation<AddTransaction>(startDestination = AddTransaction.Main()) {
        composable<AddTransaction.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val type = it.toRoute<AddTransaction.Main>().transactionType
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)
            val category by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val source by viewModel.transactionSource.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val addTransactionUiState = AddTransactionUiState(
                type = type,
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
                    navController.navigate(AddTransaction.Category(type))
                }

                override fun onNavigateToSource() {
                    navController.navigate(AddTransaction.Source)
                }

                override fun onAddNewTransaction(
                    transactionState: AddTransactionState
                ) {
                    viewModel.createNewTransaction(transactionState)
                }
            }

            AddTransactionScreen(
                transactionUiState = addTransactionUiState,
                transactionActions = addTransactionActions,
                navController = navController
            )
        }
        composable<AddTransaction.Category>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val transactionType = it.toRoute<AddTransaction.Category>().transactionType
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = transactionType,
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::changeTransactionCategory
            )
        }
        composable<AddTransaction.Source>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AddTransactionSourceScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onSourceSelect = viewModel::changeTransactionSource
            )
        }
    }
}

fun NavGraphBuilder.transferNavGraph(
    navController: NavHostController
) {
    navigation<Transfer>(startDestination = Transfer.Main) {
        composable<Transfer.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<TransferViewModel>(navController)
            val source by viewModel.sourceAccount.collectAsStateWithLifecycle()
            val destination by viewModel.destinationAccount.collectAsStateWithLifecycle()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            val transferUiState = TransferUiState(
                source = source,
                destination = destination,
                addResult = addResult
            )

            val transferActions = object : TransferActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToSourceAccount() {
                    navController.navigate(Transfer.Account(SelectAccountType.SOURCE))
                }

                override fun onNavigateToDestinationAccount() {
                    navController.navigate(Transfer.Account(SelectAccountType.DESTINATION))
                }

                override fun onAddNewTransfer(transferState: TransferState) {
                    viewModel.createNewTransfer(transferState)
                }
            }

            TransferScreen(
                transferUiState = transferUiState,
                transferActions = transferActions
            )
        }
        composable<Transfer.Account>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val args = it.toRoute<Transfer.Account>()
            val viewModel = it.sharedKoinViewModel<TransferViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()
            val selectedAccount by viewModel.selectedAccounts.collectAsStateWithLifecycle(
                initialValue = emptyList()
            )

            TransferAccountScreen(
                selectAccountType = args.type,
                accounts = accounts,
                selectedAccounts = selectedAccount,
                onNavigateBack = navController::navigateUp,
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
    navigation<TransactionDetail>(startDestination = TransactionDetail.Main()) {
        composable<TransactionDetail.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<TransactionDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val transactionState by viewModel.transactionState.collectAsStateWithLifecycle()

            transactionState?.let { transactionState ->
                val detailTransactionActions = object : TransactionDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditTransaction(
                        transactionId: Long,
                        transactionType: Int,
                        transactionCategory: Int
                    ) {
                        navController.navigate(TransactionDetail.Edit(transactionId))
                    }

                    override fun onRemoveTransaction(transactionState: TransactionState) {
                        viewModel.deleteTransaction(transactionState)
                    }
                }

                DetailTransactionScreen(
                    transactionState = transactionState,
                    transactionActions = detailTransactionActions
                )
            }
        }
        composable<TransactionDetail.Edit>(
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
                    val editTransactionUiState = EditTransactionUiState(
                        id = transaction.id,
                        title = transaction.title,
                        type = transaction.type,
                        parentCategory = transaction.parentCategory,
                        childCategory = transaction.childCategory,
                        date = transaction.date,
                        amount = transaction.amount,
                        sourceId = transaction.sourceId,
                        sourceName = transaction.sourceName,
                        editResult = editResult,
                        isLocked = transaction.isLocked
                    )

                    val editTransactionActions = object : EditTransactionActions {
                        override fun onNavigateBack() {
                            navController.navigateUp()
                            viewModel.restoreOriginalTransaction()
                        }

                        override fun onNavigateToCategory(transactionType: Int) {
                            navController.navigate(TransactionDetail.Category(transactionType))
                        }

                        override fun onEditTransaction(transactionState: EditTransactionState) {
                            viewModel.updateTransaction(transactionState)
                        }
                    }

                    EditTransactionScreen(
                        transactionUiState = editTransactionUiState,
                        transactionActions = editTransactionActions,
                        initialTransactionState = initialTransaction
                    )
                }
            }
        }
        composable<TransactionDetail.Category>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val transactionType = it.toRoute<TransactionDetail.Category>().transactionType
            val viewModel = it.sharedKoinViewModel<EditTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = transactionType,
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::changeTransactionCategory
            )
        }
    }
}