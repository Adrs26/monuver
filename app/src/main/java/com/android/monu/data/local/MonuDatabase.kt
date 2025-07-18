package com.android.monu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.local.entity.AccountEntity
import com.android.monu.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, AccountEntity::class],
    version = 7,
    exportSchema = false
)
abstract class MonuDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
}