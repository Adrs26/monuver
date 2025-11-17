package com.android.monuver.feature.transaction.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.android.monuver.core.domain.model.TransactionState
import com.android.monuver.core.domain.util.SelectAccountType
import com.android.monuver.core.presentation.components.AccountListScreen
import com.android.monuver.core.presentation.components.TransactionCategoryScreen
import com.android.monuver.core.presentation.navigation.Account
import com.android.monuver.core.presentation.navigation.AddTransaction
import com.android.monuver.core.presentation.navigation.TransactionDetail
import com.android.monuver.core.presentation.navigation.Transfer
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.core.presentation.util.sharedKoinViewModel
import com.android.monuver.feature.transaction.domain.model.AddTransactionState
import com.android.monuver.feature.transaction.domain.model.EditTransactionState
import com.android.monuver.feature.transaction.domain.model.TransferState
import com.android.monuver.feature.transaction.presentation.addTransaction.AddTransactionActions
import com.android.monuver.feature.transaction.presentation.addTransaction.AddTransactionScreen
import com.android.monuver.feature.transaction.presentation.addTransaction.AddTransactionViewModel
import com.android.monuver.feature.transaction.presentation.editTransaction.EditTransactionActions
import com.android.monuver.feature.transaction.presentation.editTransaction.EditTransactionScreen
import com.android.monuver.feature.transaction.presentation.editTransaction.EditTransactionViewModel
import com.android.monuver.feature.transaction.presentation.transactionDetail.DetailTransactionScreen
import com.android.monuver.feature.transaction.presentation.transactionDetail.TransactionDetailActions
import com.android.monuver.feature.transaction.presentation.transactionDetail.TransactionDetailViewModel
import com.android.monuver.feature.transaction.presentation.transfer.TransferActions
import com.android.monuver.feature.transaction.presentation.transfer.TransferScreen
import com.android.monuver.feature.transaction.presentation.transfer.TransferViewModel
import com.android.monuver.feature.transaction.presentation.transfer.components.TransferAccountListScreen
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

                override fun onShowWarningAlert(warning: Int, category: Int) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("warning_condition", warning)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("budget_category", category)
                }

                override fun onAddNewTransaction(transactionState: AddTransactionState) {
                    viewModel.createNewTransaction(transactionState)
                }
            }

            AddTransactionScreen(
                transactionType = type,
                transactionCategory = category,
                transactionSource = source,
                result = addResult,
                transactionActions = addTransactionActions
            )
        }
        composable<AddTransaction.Category>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val type = it.toRoute<AddTransaction.Category>().transactionType
            val viewModel = it.sharedKoinViewModel<AddTransactionViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = type,
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

            AccountListScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::changeTransactionSource,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
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
                transferSource = source,
                transferDestination = destination,
                result = addResult,
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

            TransferAccountListScreen(
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
                },
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
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
                        initialTransactionState = initialTransaction,
                        transactionState = transaction,
                        result = editResult,
                        transactionActions = editTransactionActions
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