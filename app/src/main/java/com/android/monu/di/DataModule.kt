package com.android.monu.di

import com.android.monu.data.repository.TransactionRepositoryImpl
import com.android.monu.domain.repository.TransactionRepository
import org.koin.dsl.module

val dataModule = module {
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
}