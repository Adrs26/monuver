package com.android.monu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class MonuDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}