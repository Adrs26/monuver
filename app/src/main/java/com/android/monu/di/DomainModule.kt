package com.android.monu.di

import com.android.monu.domain.usecase.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.InsertTransactionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetAllTransactionsUseCase(get()) }
    factory { InsertTransactionUseCase(get()) }
}