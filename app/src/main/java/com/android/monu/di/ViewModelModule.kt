package com.android.monu.di

import androidx.lifecycle.SavedStateHandle
import com.android.monu.presentation.screen.account.AccountViewModel
import com.android.monu.presentation.screen.account.addaccount.AddAccountViewModel
import com.android.monu.presentation.screen.analytics.AnalyticsViewModel
import com.android.monu.presentation.screen.analytics.analyticscategorytransaction.AnalyticsCategoryTransactionViewModel
import com.android.monu.presentation.screen.budgeting.BudgetingViewModel
import com.android.monu.presentation.screen.budgeting.addbudgeting.AddBudgetingViewModel
import com.android.monu.presentation.screen.budgeting.budgetingdetail.BudgetingDetailViewModel
import com.android.monu.presentation.screen.budgeting.editbudgeting.EditBudgetingViewModel
import com.android.monu.presentation.screen.budgeting.inactivebudgeting.InactiveBudgetingViewModel
import com.android.monu.presentation.screen.home.HomeViewModel
import com.android.monu.presentation.screen.transaction.TransactionViewModel
import com.android.monu.presentation.screen.transaction.addtransaction.AddTransactionViewModel
import com.android.monu.presentation.screen.transaction.transactiondetail.TransactionDetailViewModel
import com.android.monu.presentation.screen.transaction.edittransaction.EditTransactionViewModel
import com.android.monu.presentation.screen.transaction.edittransfer.EditTransferViewModel
import com.android.monu.presentation.screen.transaction.transfer.TransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { TransactionViewModel(get(), get()) }
    viewModel { BudgetingViewModel(get(), get()) }
    viewModel { AnalyticsViewModel(get(), get(), get(), get()) }

    viewModel { AccountViewModel(get(), get()) }
    viewModel { AddAccountViewModel(get()) }

    viewModel { AddTransactionViewModel(get(), get(), get()) }
    viewModel { TransferViewModel(get(), get()) }
    viewModel { (handle: SavedStateHandle) ->
        TransactionDetailViewModel(handle, get(), get(), get(), get())
    }
    viewModel { (handle: SavedStateHandle) ->
        EditTransactionViewModel(handle, get(), get(), get())
    }
    viewModel { (handle: SavedStateHandle) ->
        EditTransferViewModel(handle, get(), get())
    }

    viewModel { AddBudgetingViewModel(get()) }
    viewModel { (handle: SavedStateHandle) ->
        BudgetingDetailViewModel(handle, get(), get(), get())
    }
    viewModel { (handle: SavedStateHandle) ->
        EditBudgetingViewModel(handle, get(), get())
    }

    viewModel { InactiveBudgetingViewModel(get()) }

    viewModel { (handle: SavedStateHandle) ->
        AnalyticsCategoryTransactionViewModel(handle, get())
    }
}