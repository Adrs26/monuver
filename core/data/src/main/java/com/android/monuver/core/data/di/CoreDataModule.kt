package com.android.monuver.core.data.di

import com.android.monuver.core.data.datastore.UserPreferences
import com.android.monuver.core.data.repository.CoreRepositoryImpl
import com.android.monuver.core.domain.common.CustomDispatcher
import com.android.monuver.core.domain.common.CustomDispatcherImpl
import com.android.monuver.core.domain.repository.CoreRepository
import com.android.monuver.core.domain.usecase.CheckAppVersionUseCase
import com.android.monuver.core.domain.usecase.GetActiveAccountsUseCase
import com.android.monuver.core.domain.usecase.GetBudgetSummaryUseCase
import com.android.monuver.core.domain.usecase.GetDistinctTransactionYearsUseCase
import com.android.monuver.core.domain.usecase.GetUnpaidBillsUseCase
import com.android.monuver.core.domain.usecase.HandleExpiredBudgetUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::UserPreferences)
    singleOf(::CoreRepositoryImpl) { bind<CoreRepository>() }
    singleOf(::CustomDispatcherImpl) { bind<CustomDispatcher>() }

    factoryOf(::CheckAppVersionUseCase)
    factoryOf(::GetActiveAccountsUseCase)
    factoryOf(::GetBudgetSummaryUseCase)
    factoryOf(::GetDistinctTransactionYearsUseCase)
    factoryOf(::GetUnpaidBillsUseCase)
    factoryOf(::HandleExpiredBudgetUseCase)
}