package com.android.monu.di

import com.android.monu.ui.feature.screen.MainViewModel
import com.android.monu.ui.feature.screen.account.AccountViewModel
import com.android.monu.ui.feature.screen.account.addaccount.AddAccountViewModel
import com.android.monu.ui.feature.screen.analytics.AnalyticsViewModel
import com.android.monu.ui.feature.screen.analytics.analyticscategorytransaction.AnalyticsCategoryTransactionViewModel
import com.android.monu.ui.feature.screen.billing.BillingViewModel
import com.android.monu.ui.feature.screen.billing.addbill.AddBillViewModel
import com.android.monu.ui.feature.screen.billing.billdetail.BillDetailViewModel
import com.android.monu.ui.feature.screen.billing.editbill.EditBillViewModel
import com.android.monu.ui.feature.screen.billing.paybill.PayBillViewModel
import com.android.monu.ui.feature.screen.budgeting.BudgetingViewModel
import com.android.monu.ui.feature.screen.budgeting.addbudget.AddBudgetViewModel
import com.android.monu.ui.feature.screen.budgeting.budgetdetail.BudgetDetailViewModel
import com.android.monu.ui.feature.screen.budgeting.editbudget.EditBudgetViewModel
import com.android.monu.ui.feature.screen.budgeting.inactivebudget.InactiveBudgetViewModel
import com.android.monu.ui.feature.screen.home.HomeViewModel
import com.android.monu.ui.feature.screen.saving.SavingViewModel
import com.android.monu.ui.feature.screen.saving.addsave.AddSaveViewModel
import com.android.monu.ui.feature.screen.saving.deposit.DepositViewModel
import com.android.monu.ui.feature.screen.saving.savedetail.SaveDetailViewModel
import com.android.monu.ui.feature.screen.settings.SettingsViewModel
import com.android.monu.ui.feature.screen.transaction.TransactionViewModel
import com.android.monu.ui.feature.screen.transaction.addtransaction.AddTransactionViewModel
import com.android.monu.ui.feature.screen.transaction.edittransaction.EditTransactionViewModel
import com.android.monu.ui.feature.screen.transaction.transactiondetail.TransactionDetailViewModel
import com.android.monu.ui.feature.screen.transaction.transfer.TransferViewModel
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
    viewModelOf(::AddSaveViewModel)
    viewModelOf(::SaveDetailViewModel)
    viewModelOf(::DepositViewModel)

    viewModelOf(::SettingsViewModel)
}