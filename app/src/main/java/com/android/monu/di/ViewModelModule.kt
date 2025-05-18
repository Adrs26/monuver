package com.android.monu.di

import com.android.monu.presentation.screen.transactions.TransactionsViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddExpenseViewModel
import com.android.monu.presentation.screen.transactions.transaction.AddIncomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TransactionsViewModel(get()) }
    viewModel { AddIncomeViewModel(get()) }
    viewModel { AddExpenseViewModel(get()) }
}