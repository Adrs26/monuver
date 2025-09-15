package com.android.monu.ui.feature.screen.billing

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monu.ui.feature.screen.billing.addBill.AddBillScreen
import com.android.monu.ui.feature.screen.billing.addBill.AddBillViewModel
import com.android.monu.ui.feature.screen.billing.billDetail.BillDetailActions
import com.android.monu.ui.feature.screen.billing.billDetail.BillDetailScreen
import com.android.monu.ui.feature.screen.billing.billDetail.BillDetailViewModel
import com.android.monu.ui.feature.screen.billing.editBill.EditBillScreen
import com.android.monu.ui.feature.screen.billing.editBill.EditBillState
import com.android.monu.ui.feature.screen.billing.editBill.EditBillViewModel
import com.android.monu.ui.feature.screen.billing.payBill.PayBillActions
import com.android.monu.ui.feature.screen.billing.payBill.PayBillScreen
import com.android.monu.ui.feature.screen.billing.payBill.PayBillState
import com.android.monu.ui.feature.screen.billing.payBill.PayBillViewModel
import com.android.monu.ui.feature.screen.billing.payBill.components.PayBillContentState
import com.android.monu.ui.feature.screen.transaction.addTransaction.components.AddTransactionSourceScreen
import com.android.monu.ui.feature.screen.transaction.components.TransactionCategoryScreen
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.NavigationAnimation
import com.android.monu.ui.feature.utils.TransactionType
import com.android.monu.ui.feature.utils.sharedKoinViewModel
import com.android.monu.ui.navigation.Billing
import com.android.monu.ui.navigation.PayBill
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.billingNavGraph(
    navController: NavHostController
) {
    navigation<Billing>(startDestination = Billing.Main) {
        composable<Billing.Main>(
            enterTransition = { NavigationAnimation.enter },
            exitTransition = { NavigationAnimation.exit },
            popEnterTransition = { NavigationAnimation.popEnter },
            popExitTransition = { NavigationAnimation.popExit }
        ) {
            val viewModel = koinViewModel<BillingViewModel>()
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
                    navController.navigate(Billing.Add)
                }

                override fun onNavigateToBillDetail(billId: Long) {
                    navController.navigate(Billing.Detail(billId))
                }
            }

            BillingScreen(
                billState = billState,
                billActions = billActions
            )
        }
        composable<Billing.Add>(
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
        composable<Billing.Detail>(
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
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            bill?.let { bill ->
                val billDetailActions = object : BillDetailActions {
                    override fun onNavigateBack() {
                        navController.navigateUp()
                    }

                    override fun onNavigateToEditBill(billId: Long) {
                        navController.navigate(Billing.Edit(billId))
                    }

                    override fun onRemoveBill(billId: Long) {
                        viewModel.deleteBill(billId)
                    }

                    override fun onNavigateToPayBill(billId: Long) {
                        navController.navigate(PayBill.Main(billId))
                    }

                    override fun onCancelBillPayment(billId: Long) {
                        viewModel.cancelBillPayment(billId)
                    }
                }

                BillDetailScreen(
                    bill = bill,
                    editResult = editResult,
                    billDetailActions = billDetailActions
                )
            }
        }
        composable<Billing.Edit>(
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
                    parentId = bill.parentId,
                    title = bill.title,
                    date = bill.dueDate,
                    amount = bill.amount,
                    timeStamp = bill.timeStamp,
                    isRecurring = bill.isRecurring,
                    cycle = bill.cycle ?: Cycle.YEARLY,
                    period = bill.period ?: 1,
                    fixPeriod = (bill.fixPeriod ?: "").toString(),
                    nowPaidPeriod = bill.nowPaidPeriod,
                    isPaidBefore = bill.isPaidBefore,
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
    navigation<PayBill>(startDestination = PayBill.Main()) {
        composable<PayBill.Main>(
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
                        navController.navigate(PayBill.Category)
                    }

                    override fun onNavigateToSource() {
                        navController.navigate(PayBill.Source)
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
        composable<PayBill.Category>(
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
        composable<PayBill.Source>(
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