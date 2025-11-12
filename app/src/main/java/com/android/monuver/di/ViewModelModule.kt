package com.android.monuver.di

import com.android.monuver.ui.feature.screen.MainViewModel
import com.android.monuver.ui.feature.screen.account.AccountViewModel
import com.android.monuver.ui.feature.screen.account.accountDetail.AccountDetailViewModel
import com.android.monuver.ui.feature.screen.account.addAccount.AddAccountViewModel
import com.android.monuver.ui.feature.screen.account.editAccount.EditAccountViewModel
import com.android.monuver.ui.feature.screen.analytics.AnalyticsViewModel
import com.android.monuver.ui.feature.screen.analytics.analyticsCategoryTransaction.AnalyticsCategoryTransactionViewModel
import com.android.monuver.ui.feature.screen.billing.BillingViewModel
import com.android.monuver.ui.feature.screen.billing.addBill.AddBillViewModel
import com.android.monuver.ui.feature.screen.billing.billDetail.BillDetailViewModel
import com.android.monuver.ui.feature.screen.billing.editBill.EditBillViewModel
import com.android.monuver.ui.feature.screen.billing.payBill.PayBillViewModel
import com.android.monuver.ui.feature.screen.budgeting.BudgetingViewModel
import com.android.monuver.ui.feature.screen.budgeting.addBudget.AddBudgetViewModel
import com.android.monuver.ui.feature.screen.budgeting.budgetDetail.BudgetDetailViewModel
import com.android.monuver.ui.feature.screen.budgeting.editBudget.EditBudgetViewModel
import com.android.monuver.ui.feature.screen.budgeting.inactiveBudget.InactiveBudgetViewModel
import com.android.monuver.ui.feature.screen.home.HomeViewModel
import com.android.monuver.ui.feature.screen.saving.SavingViewModel
import com.android.monuver.ui.feature.screen.saving.addSaving.AddSavingViewModel
import com.android.monuver.ui.feature.screen.saving.deposit.DepositViewModel
import com.android.monuver.ui.feature.screen.saving.editSaving.EditSavingViewModel
import com.android.monuver.ui.feature.screen.saving.inactiveSaving.InactiveSavingViewModel
import com.android.monuver.ui.feature.screen.saving.savingDetail.SavingDetailViewModel
import com.android.monuver.ui.feature.screen.saving.withdraw.WithdrawViewModel
import com.android.monuver.ui.feature.screen.settings.SettingsViewModel
import com.android.monuver.ui.feature.screen.transaction.TransactionViewModel
import com.android.monuver.ui.feature.screen.transaction.addTransaction.AddTransactionViewModel
import com.android.monuver.ui.feature.screen.transaction.editTransaction.EditTransactionViewModel
import com.android.monuver.ui.feature.screen.transaction.transactionDetail.TransactionDetailViewModel
import com.android.monuver.ui.feature.screen.transaction.transfer.TransferViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)

    viewModelOf(::HomeViewModel)
    viewModelOf(::TransactionViewModel)
    viewModelOf(::BudgetingViewModel)
    viewModelOf(::AnalyticsViewModel)

    viewModelOf(::AccountViewModel)
    viewModelOf(::AddAccountViewModel)
    viewModelOf(::AccountDetailViewModel)
    viewModelOf(::EditAccountViewModel)

    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::TransferViewModel)
    viewModelOf(::TransactionDetailViewModel)
    viewModelOf(::EditTransactionViewModel)

    viewModelOf(::AddBudgetViewModel)
    viewModelOf(::BudgetDetailViewModel)
    viewModelOf(::EditBudgetViewModel)
    viewModelOf(::InactiveBudgetViewModel)

    viewModelOf(::AnalyticsCategoryTransactionViewModel)

    viewModelOf(::BillingViewModel)
    viewModelOf(::AddBillViewModel)
    viewModelOf(::BillDetailViewModel)
    viewModelOf(::PayBillViewModel)
    viewModelOf(::EditBillViewModel)

    viewModelOf(::SavingViewModel)
    viewModelOf(::AddSavingViewModel)
    viewModelOf(::SavingDetailViewModel)
    viewModelOf(::EditSavingViewModel)
    viewModelOf(::DepositViewModel)
    viewModelOf(::WithdrawViewModel)
    viewModelOf(::InactiveSavingViewModel)

    viewModelOf(::SettingsViewModel)
}