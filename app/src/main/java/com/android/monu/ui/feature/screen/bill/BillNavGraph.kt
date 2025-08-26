package com.android.monu.ui.feature.screen.bill

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.ui.feature.screen.bill.addbill.AddBillScreen
import com.android.monu.ui.feature.screen.bill.addbill.AddBillViewModel
import com.android.monu.ui.feature.screen.bill.billdetail.BillDetailActions
import com.android.monu.ui.feature.screen.bill.billdetail.BillDetailScreen
import com.android.monu.ui.feature.screen.bill.billdetail.BillDetailViewModel
import com.android.monu.ui.feature.screen.bill.editbill.EditBillScreen
import com.android.monu.ui.feature.screen.bill.editbill.EditBillState
import com.android.monu.ui.feature.screen.bill.editbill.EditBillViewModel
import com.android.monu.ui.feature.screen.bill.paybill.PayBillActions
import com.android.monu.ui.feature.screen.bill.paybill.PayBillScreen
import com.android.monu.ui.feature.screen.bill.paybill.PayBillState
import com.android.monu.ui.feature.screen.bill.paybill.PayBillViewModel
import com.android.monu.ui.feature.screen.bill.paybill.components.PayBillContentState
import com.android.monu.ui.feature.screen.transaction.addtransaction.components.AddTransactionSourceScreen
import com.android.monu.ui.feature.screen.transaction.components.TransactionCategoryScreen
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.TransactionType
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.AddBill
import com.android.monu.ui.navigation.Bill
import com.android.monu.ui.navigation.BillDetail
import com.android.monu.ui.navigation.EditBill
import com.android.monu.ui.navigation.MainBill
import com.android.monu.ui.navigation.MainPayBill
import com.android.monu.ui.navigation.PayBill
import com.android.monu.ui.navigation.PayBillCategory
import com.android.monu.ui.navigation.PayBillSource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.billNavGraph(
    navController: NavHostController
) {
    navigation<Bill>(startDestination = MainBill) {
        composable<MainBill>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<BillViewModel>()
            val pendingBills by viewModel.pendingBills.collectAsStateWithLifecycle()
            val dueBills by viewModel.dueBills.collectAsStateWithLifecycle()
            val paidBills = viewModel.paidBills.collectAsLazyPagingItems()

            val billState = BillState(
                pendingBills = pendingBills,
                dueBills = dueBills,
                paidBills = paidBills
            )

            val billActions = object : BillActions {
                override fun onNavigateBack() {
                    navController.navigateUp()
                }

                override fun onNavigateToAddBill() {
                    navController.navigate(AddBill)
                }

                override fun onNavigateToBillDetail(billId: Long) {
                    navController.navigate(BillDetail(billId))
                }
            }

            BillScreen(
                billState = billState,
                billActions = billActions
            )
        }
        composable<AddBill>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<AddBillViewModel>()
            val addResult by viewModel.createResult.collectAsStateWithLifecycle()

            AddBillScreen(
                addResult = addResult,
                onNavigateBack = navController::navigateUp,
                onAddNewBill = viewModel::createNewBill
            )
        }
        composable<BillDetail>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<BillDetailViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val bill by viewModel.bill.collectAsStateWithLifecycle()

            bill?.let { bill ->
                val billDetailActions = object : BillDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditBill(billId: Long) {
                        navController.navigate(EditBill(billId))
                    }

                    override fun onRemoveBill(billId: Long) {
                        viewModel.deleteBill(billId)
                    }

                    override fun onNavigateToPayBill(billId: Long) {
                        navController.navigate(MainPayBill(billId))
                    }
                }

                BillDetailScreen(
                    bill = bill,
                    billDetailActions = billDetailActions
                )
            }
        }
        composable<EditBill>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<EditBillViewModel>(
                viewModelStoreOwner = it,
                parameters = { parametersOf(it.savedStateHandle) }
            )
            val bill by viewModel.bill.collectAsStateWithLifecycle()
            val updateResult by viewModel.updateResult.collectAsStateWithLifecycle()

            bill?.let { bill ->
                val editBillState = EditBillState(
                    id = bill.id,
                    title = bill.title,
                    date = bill.dueDate,
                    amount = bill.amount,
                    isRecurring = bill.isRecurring,
                    cycle = bill.cycle ?: Cycle.YEARLY,
                    period = bill.period ?: 1,
                    fixPeriod = (bill.fixPeriod ?: "").toString(),
                    nowPaidPeriod = bill.nowPaidPeriod,
                    editResult = updateResult
                )

                EditBillScreen(
                    billState = editBillState,
                    onNavigateBack = navController::navigateUp,
                    onEditBill = viewModel::updateBill
                )
            }
        }
    }
}

fun NavGraphBuilder.payBillNavGraph(
    navController: NavHostController
) {
    navigation<PayBill>(startDestination = MainPayBill()) {
        composable<MainPayBill>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<PayBillViewModel>(navController) {
                parametersOf(it.savedStateHandle)
            }
            val bill by viewModel.bill.collectAsStateWithLifecycle()
            val transactionCategory by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val transactionSource by viewModel.transactionSource.collectAsStateWithLifecycle()
            val payResult by viewModel.payResult.collectAsStateWithLifecycle()

            bill?.let { bill ->
                val payBillState = PayBillState(
                    category = transactionCategory,
                    source = transactionSource,
                    payResult = payResult
                )

                val payBillActions = object : PayBillActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToCategory() {
                        navController.navigate(PayBillCategory)
                    }

                    override fun onNavigateToSource() {
                        navController.navigate(PayBillSource)
                    }

                    override fun onPayBill(billState: PayBillContentState) {
                        viewModel.payBill(bill, billState)
                    }
                }

                PayBillScreen(
                    bill = bill,
                    payBillState = payBillState,
                    payBillActions = payBillActions
                )
            }
        }
        composable<PayBillCategory>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<PayBillViewModel>(navController)

            TransactionCategoryScreen(
                transactionType = TransactionType.EXPENSE,
                onNavigateBack = navController::navigateUp,
                onCategorySelect = viewModel::changeTransactionCategory
            )
        }
        composable<PayBillSource>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = it.sharedKoinViewModel<PayBillViewModel>(navController)
            val accounts by viewModel.accounts.collectAsStateWithLifecycle()

            AddTransactionSourceScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onSourceSelect = viewModel::changeTransactionSource
            )
        }
    }
}