package com.android.monu.di

import androidx.lifecycle.SavedStateHandle
import com.android.monu.presentation.screen.account.AccountViewModel
import com.android.monu.presentation.screen.account.addaccount.AddAccountViewModel
import com.android.monu.presentation.screen.analytics.AnalyticsViewModel
import com.android.monu.presentation.screen.transaction.TransactionViewModel
import com.android.monu.presentation.screen.transaction.addtransaction.AddTransactionViewModel
import com.android.monu.presentation.screen.transaction.detailtransaction.DetailTransactionViewModel
import com.android.monu.presentation.screen.transaction.transfer.TransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AccountViewModel(get(), get()) }
    viewModel { AddAccountViewModel(get()) }
    viewModel { TransactionViewModel(get(), get()) }
    viewModel { (handle: SavedStateHandle) ->
        DetailTransactionViewModel(get(), get(), get(), get(), handle)
    }
    viewModel { AddTransactionViewModel(get(), get(), get()) }
    viewModel { TransferViewModel(get(), get()) }
    viewModel { AnalyticsViewModel(get(), get(), get(), get()) }
}