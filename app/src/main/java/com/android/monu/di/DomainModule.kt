package com.android.monu.di

import com.android.monu.domain.usecase.DeleteTransactionByIdUseCase
import com.android.monu.domain.usecase.GetAllTransactionsUseCase
import com.android.monu.domain.usecase.GetAvailableTransactionYearsUseCase
import com.android.monu.domain.usecase.GetTransactionByIdUseCase
import com.android.monu.domain.usecase.InsertTransactionUseCase
import com.android.monu.domain.usecase.UpdateTransactionUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetAllTransactionsUseCase(get()) }
    factory { GetTransactionByIdUseCase(get()) }
    factory { GetAvailableTransactionYearsUseCase(get()) }
    factory { InsertTransactionUseCase(get()) }
    factory { UpdateTransactionUseCase(get()) }
    factory { DeleteTransactionByIdUseCase(get()) }
}