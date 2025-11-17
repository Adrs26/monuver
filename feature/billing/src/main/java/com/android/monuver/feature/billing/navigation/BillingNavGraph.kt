package com.android.monuver.feature.billing.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.core.presentation.components.AccountListScreen
import com.android.monuver.core.presentation.components.TransactionCategoryScreen
import com.android.monuver.core.presentation.navigation.Account
import com.android.monuver.core.presentation.navigation.Billing
import com.android.monuver.core.presentation.navigation.PayBill
import com.android.monuver.core.presentation.util.NavigationAnimation
import com.android.monuver.core.presentation.util.sharedKoinViewModel
import com.android.monuver.feature.billing.domain.model.PayBillState
import com.android.monuver.feature.billing.presentation.BillActions
import com.android.monuver.feature.billing.presentation.BillUiState
import com.android.monuver.feature.billing.presentation.BillingScreen
import com.android.monuver.feature.billing.presentation.BillingViewModel
import com.android.monuver.feature.billing.presentation.addBill.AddBillScreen
import com.android.monuver.feature.billing.presentation.addBill.AddBillViewModel
import com.android.monuver.feature.billing.presentation.billDetail.BillDetailActions
import com.android.monuver.feature.billing.presentation.billDetail.BillDetailScreen
import com.android.monuver.feature.billing.presentation.billDetail.BillDetailViewModel
import com.android.monuver.feature.billing.presentation.editBill.EditBillScreen
import com.android.monuver.feature.billing.presentation.editBill.EditBillViewModel
import com.android.monuver.feature.billing.presentation.payBill.PayBillActions
import com.android.monuver.feature.billing.presentation.payBill.PayBillScreen
import com.android.monuver.feature.billing.presentation.payBill.PayBillViewModel
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
                result = addResult,
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
                    result = editResult,
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
                EditBillScreen(
                    billState = billState,
                    result = updateResult,
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
            val category by viewModel.transactionCategory.collectAsStateWithLifecycle()
            val source by viewModel.transactionSource.collectAsStateWithLifecycle()
            val payResult by viewModel.payResult.collectAsStateWithLifecycle()

            bill?.let { bill ->
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
                    category = category,
                    billAmount = bill.amount,
                    source = source,
                    result = payResult,
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

            AccountListScreen(
                accounts = accounts,
                onNavigateBack = navController::navigateUp,
                onAccountSelect = viewModel::changeTransactionSource,
                onNavigateToAddAccount = { navController.navigate(Account.Add) }
            )
        }
    }
}