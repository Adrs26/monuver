package com.android.monu.di

import com.android.monu.ui.worker.ReminderWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val workerModule = module {
    workerOf(::ReminderWorker)
}