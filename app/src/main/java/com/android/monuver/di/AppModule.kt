package com.android.monuver.di

import com.android.monuver.MainViewModel
import com.android.monuver.NotificationNavigatorImpl
import com.android.monuver.core.data.di.coreDataModule
import com.android.monuver.core.data.di.coreDatabaseModule
import com.android.monuver.core.presentation.navigation.NotificationNavigator
import com.android.monuver.feature.account.di.accountModule
import com.android.monuver.feature.analytics.di.analyticsModule
import com.android.monuver.feature.billing.di.billingModule
import com.android.monuver.feature.budgeting.di.budgetingModule
import com.android.monuver.feature.home.di.homeModule
import com.android.monuver.feature.saving.di.savingModule
import com.android.monuver.feature.settings.di.settingsModule
import com.android.monuver.feature.transaction.di.transactionModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = listOf(
    accountModule,
    analyticsModule,
    billingModule,
    budgetingModule,
    coreDatabaseModule,
    coreDataModule,
    homeModule,
    savingModule,
    settingsModule,
    transactionModule,

    module {
        singleOf(::NotificationNavigatorImpl) { bind<NotificationNavigator>() }
        viewModelOf(::MainViewModel)
    }
)