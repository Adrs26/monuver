package com.android.monu.di

import androidx.room.Room
import com.android.monu.data.local.MonuDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
                androidContext(),
                MonuDatabase::class.java,
                "MonuDatabase.db"
            ).fallbackToDestructiveMigration(true).build()
    }
    single { get<MonuDatabase>().transactionDao() }
    single { get<MonuDatabase>().accountDao() }
    single { get<MonuDatabase>().financeDao() }
}