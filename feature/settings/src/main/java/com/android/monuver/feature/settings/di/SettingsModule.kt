package com.android.monuver.feature.settings.di

import com.android.monuver.feature.settings.data.manager.DataManagerImpl
import com.android.monuver.feature.settings.data.repository.SettingsRepositoryImpl
import com.android.monuver.feature.settings.domain.manager.DataManager
import com.android.monuver.feature.settings.domain.repository.SettingsRepository
import com.android.monuver.feature.settings.domain.usecase.BackupDataUseCase
import com.android.monuver.feature.settings.domain.usecase.DeleteAllDataUseCase
import com.android.monuver.feature.settings.domain.usecase.ExportDataToPdfUseCase
import com.android.monuver.feature.settings.domain.usecase.RestoreDataUseCase
import com.android.monuver.feature.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    singleOf(::DataManagerImpl) { bind<DataManager>() }
    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }

    factoryOf(::BackupDataUseCase)
    factoryOf(::DeleteAllDataUseCase)
    factoryOf(::ExportDataToPdfUseCase)
    factoryOf(::RestoreDataUseCase)

    viewModelOf(::SettingsViewModel)
}