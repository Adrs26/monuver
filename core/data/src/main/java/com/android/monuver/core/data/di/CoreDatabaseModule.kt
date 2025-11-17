package com.android.monuver.core.data.di

import androidx.room.Room
import com.android.monuver.core.data.database.MonuverDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDatabaseModule = module {
    single {
        Room.databaseBuilder(
                androidContext(),
                MonuverDatabase::class.java,
                "MonuverDatabase.db"
            )
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single { get<MonuverDatabase>().transactionDao() }
    single { get<MonuverDatabase>().accountDao() }
    single { get<MonuverDatabase>().budgetDao() }
    single { get<MonuverDatabase>().saveDao() }
    single { get<MonuverDatabase>().billDao() }
}