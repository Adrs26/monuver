package com.android.monu

import android.app.Application
import com.android.monu.di.appModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MonuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MonuApplication)
            modules(appModule)
        }
        AndroidThreeTen.init(this)
    }
}