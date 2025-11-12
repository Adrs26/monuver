package com.android.monuver

import android.app.Application
import com.android.monuver.di.appModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class MonuverApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MonuverApplication)
            modules(appModule)
            workManagerFactory()
        }
        AndroidThreeTen.init(this)
    }
}