package com.android.monuver.di

import com.android.monuver.ui.worker.ReminderWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val workerModule = module {
    workerOf(::ReminderWorker)
}