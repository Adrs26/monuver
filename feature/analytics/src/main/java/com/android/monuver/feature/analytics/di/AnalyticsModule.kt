package com.android.monuver.feature.analytics.di

import com.android.monuver.feature.analytics.data.repository.AnalyticsRepositoryImpl
import com.android.monuver.feature.analytics.domain.repository.AnalyticsRepository
import com.android.monuver.feature.analytics.domain.usecase.GetAllTransactionsByCategoryUseCase
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionBalanceSummaryUseCase
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionCategorySummaryUseCase
import com.android.monuver.feature.analytics.domain.usecase.GetTransactionSummaryUseCase
import com.android.monuver.feature.analytics.presentation.AnalyticsViewModel
import com.android.monuver.feature.analytics.presentation.analyticsCategoryTransaction.AnalyticsCategoryTransactionViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::AnalyticsRepositoryImpl) { bind<AnalyticsRepository>() }

    factoryOf(::GetAllTransactionsByCategoryUseCase)
    factoryOf(::GetTransactionBalanceSummaryUseCase)
    factoryOf(::GetTransactionCategorySummaryUseCase)
    factoryOf(::GetTransactionSummaryUseCase)

    viewModelOf(::AnalyticsViewModel)
    viewModelOf(::AnalyticsCategoryTransactionViewModel)
}