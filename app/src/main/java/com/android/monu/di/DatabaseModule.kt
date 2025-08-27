package com.android.monu.di

import android.content.Context
import androidx.room.Room
import com.android.monu.data.local.MonuDatabase
import com.android.monu.utils.SqlCipherKeyManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        System.loadLibrary("sqlcipher")
        val sharedPreferences = androidContext().getSharedPreferences("monu", Context.MODE_PRIVATE)
        val sqlCipherKeyManager = SqlCipherKeyManager(sharedPreferences)

        Room.databaseBuilder(
                androidContext(),
                MonuDatabase::class.java,
                "MonuDatabase.db"
            )
            .openHelperFactory(sqlCipherKeyManager.getSupportFactory())
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single { get<MonuDatabase>().transactionDao() }
    single { get<MonuDatabase>().accountDao() }
    single { get<MonuDatabase>().budgetDao() }
    single { get<MonuDatabase>().saveDao() }
    single { get<MonuDatabase>().billDao() }
}