package com.android.monuver

import android.app.Application
import com.android.monuver.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

internal class MonuverApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MonuverApp)
            modules(appModule)
            workManagerFactory()
        }
    }
}