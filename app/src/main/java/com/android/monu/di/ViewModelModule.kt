package com.android.monu.di

import androidx.lifecycle.SavedStateHandle
import com.android.monu.presentation.screen.reports.ReportsViewModel
import com.android.monu.presentation.screen.transactions.TransactionsViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddExpenseViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddIncomeViewModel
import com.android.monu.presentation.screen.transactions.transaction.EditTransactionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TransactionsViewModel(get(), get()) }
    viewModel { AddIncomeViewModel(get()) }
    viewModel { AddExpenseViewModel(get()) }
    viewModel { (handle: SavedStateHandle) ->
        EditTransactionViewModel(get(), get(), get(), handle)
    }
    viewModel { ReportsViewModel(get(), get()) }
}