package com.android.monu.di

import androidx.lifecycle.SavedStateHandle
import com.android.monu.presentation.screen.analytics.AnalyticsViewModel
import com.android.monu.presentation.screen.home.HomeViewModel
import com.android.monu.presentation.screen.report.ReportViewModel
import com.android.monu.presentation.screen.transaction.TransactionsViewModel
import com.android.monu.presentation.screen.transaction.add_expense.AddExpenseViewModel
import com.android.monu.presentation.screen.transaction.add_income.AddIncomeViewModel
import com.android.monu.presentation.screen.transaction.edit_transaction.EditTransactionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { TransactionsViewModel(get(), get()) }
    viewModel { AddIncomeViewModel(get()) }
    viewModel { AddExpenseViewModel(get()) }
    viewModel { (handle: SavedStateHandle) ->
        EditTransactionViewModel(get(), get(), get(), handle)
    }
    viewModel { ReportViewModel(get(), get()) }
    viewModel { AnalyticsViewModel(get(), get(), get(), get()) }
}