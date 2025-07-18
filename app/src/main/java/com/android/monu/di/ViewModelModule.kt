package com.android.monu.di

import androidx.lifecycle.SavedStateHandle
import com.android.monu.presentation.screen.account.AccountViewModel
import com.android.monu.presentation.screen.account.addaccount.AddAccountViewModel
import com.android.monu.presentation.screen.home.HomeViewModel
import com.android.monu.presentation.screen.transaction.TransactionViewModel
import com.android.monu.presentation.screen.transaction.addtransaction.AddTransactionViewModel
import com.android.monu.presentation.screen.transaction.detailtransaction.DetailTransactionViewModel
import com.android.monu.presentation.screen.transaction.transfer.TransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { AccountViewModel(get(), get()) }
    viewModel { AddAccountViewModel(get()) }
    viewModel { TransactionViewModel(get(), get()) }
    viewModel { (handle: SavedStateHandle) ->
        DetailTransactionViewModel(get(), get(), handle)
    }
    viewModel { AddTransactionViewModel(get(), get()) }
    viewModel { TransferViewModel(get(), get()) }
}