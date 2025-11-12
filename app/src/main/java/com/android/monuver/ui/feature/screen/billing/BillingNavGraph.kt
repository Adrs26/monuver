package com.android.monuver.ui.feature.screen.billing

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monuver.domain.model.PayBillState
import com.android.monuver.ui.feature.screen.billing.addBill.AddBillScreen
import com.android.monuver.ui.feature.screen.billing.addBill.AddBillViewModel
import com.android.monuver.ui.feature.screen.billing.billDetail.BillDetailActions
import com.android.monuver.ui.feature.screen.billing.billDetail.BillDetailScreen
import com.android.monuver.ui.feature.screen.billing.billDetail.BillDetailViewModel
import com.android.monuver.ui.feature.screen.billing.editBill.EditBillScreen
import com.android.monuver.ui.feature.screen.billing.editBill.EditBillUiState
import com.android.monuver.ui.feature.screen.billing.editBill.EditBillViewModel
import com.android.monuver.ui.feature.screen.billing.payBill.PayBillActions
import com.android.monuver.ui.feature.screen.billing.payBill.PayBillScreen
import com.android.monuver.ui.feature.screen.billing.payBill.PayBillUiState
import com.android.monuver.ui.feature.screen.billing.payBill.PayBillViewModel
import com.android.monuver.ui.feature.screen.transaction.addTransaction.components.AddTransactionSourceScreen
import com.android.monuver.ui.feature.screen.transaction.components.TransactionCategoryScreen
import com.android.monuver.ui.feature.utils.NavigationAnimation
import com.android.monuver.ui.feature.utils.sharedKoinViewModel
import com.android.monuver.ui.navigation.Account
import com.android.monuver.ui.navigation.Billing
import com.android.monuver.ui.navigation.PayBill
import com.android.monuver.utils.Cycle
import com.android.monuver.utils.TransactionType
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
            val reminderDaysBeforeDue by viewModel.reminderDaysBeforeDue.collectAsStateWithLifecycle()
            val isReminderBeforeDueDayEnabled by viewModel.isReminderBeforeDueDayEnabled
                .collectAsStateWithLifecycle()
            val isReminderForDueBillEnabled by viewModel.isReminderForDueBillEnabled
                .collectAsStateWithLifecycle()

            val billUiState = BillUiState(
                pendingBills = pendingBills,
                dueBills = dueBills,
                paidBills = paidBills,
                reminderDaysBeforeDue = reminderDaysBeforeDue,
                isReminderBeforeDueDayEnabled = isReminderBeforeDueDayEnabled,
                isReminderForDueBillEnabled = isReminderForDueBillEnabled
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

                override fun onSettingsApply(
                    reminderDaysBeforeDue: Int,
                    isReminderBeforeDueDayEnabled: Boolean,
                    isReminderForDueBillEnabled: Boolean
                ) {
                    viewModel.setReminderSettings(
                        reminderDaysBeforeDue = reminderDaysBeforeDue,
                        isReminderBeforeDueDayEnabled = isReminderBeforeDueDayEnabled,
                        isReminderForDueBillEnabled = isReminderForDueBillEnabled
                    )
                }
            }

            BillingScreen(
                billUiState = billUiState,
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
            val billState by viewModel.billState.collectAsStateWithLifecycle()
            val editResult by viewModel.updateResult.collectAsStateWithLifecycle()

            billState?.let { billState ->
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
                    billState = billState,
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
            val billState by viewModel.billState.collectAsStateWithLifecycle()
            val updateResult by viewModel.updateResult.collectAsStateWithLifecycle()

            billState?.let { billState ->
                val editBillUiState = EditBillUiState(
                    id = billState.id,
                    parentId = billState.parentId,
                    title = billState.title,
                    date = billState.dueDate,
                    amount = billState.amount,
                    timeStamp = billState.timeStamp,
                    isRecurring = billState.isRecurring,
                    cycle = billState.cycle ?: Cycle.YEARLY,
                    period = billState.period ?: 1,
                    fixPeriod = (billState.fixPeriod ?: "").toString(),
                    nowPaidPeriod = billState.nowPaidPeriod,
                    isPaidBefore = billState.isPaidBefore,
                    editResult = updateResult
                )

                EditBillScreen(
                    billUiState = editBillUiState,
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
            val bill by viewModel.billState.collectAsStateWithLifecycle()
            val transactionCategory by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val transactionSource by viewModel.transactionSource.collectAsStateWithLifecycle()
            val payResult by viewModel.payResult.collectAsStateWithLifecycle()

            bill?.let { bill ->
                val payBillUiState = PayBillUiState(
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

                    override fun onPayBill(billState: PayBillState) {
                        viewModel.payBill(bill, billState)
                    }
                }

                PayBillScreen(
                    billAmount = bill.amount,
                    payBillUiState = payBillUiState,
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
                onSourceSelect = viewModel::changeTransactionSource,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }
    }
}