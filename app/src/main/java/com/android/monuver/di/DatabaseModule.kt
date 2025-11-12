package com.android.monuver.di

import androidx.room.Room
import com.android.monuver.data.local.MonuverDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
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