package com.android.monuver.feature.home.di

import com.android.monuver.feature.home.data.repository.HomeRepositoryImpl
import com.android.monuver.feature.home.domain.repository.HomeRepository
import com.android.monuver.feature.home.domain.usecase.GetActiveAccountBalanceUseCase
import com.android.monuver.feature.home.domain.usecase.GetRecentTransactionsUseCase
import com.android.monuver.feature.home.presentation.HomeViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeRepositoryImpl) { bind<HomeRepository>() }

    factoryOf(::GetActiveAccountBalanceUseCase)
    factoryOf(::GetRecentTransactionsUseCase)

    viewModelOf(::HomeViewModel)
}