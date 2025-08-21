package com.android.monu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.local.dao.BillDao
import com.android.monu.data.local.dao.BudgetDao
import com.android.monu.data.local.dao.SaveDao
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.local.entity.room.AccountEntity
import com.android.monu.data.local.entity.room.BillEntity
import com.android.monu.data.local.entity.room.BudgetEntity
import com.android.monu.data.local.entity.room.SaveEntity
import com.android.monu.data.local.entity.room.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        BudgetEntity::class,
        SaveEntity::class,
        BillEntity::class
               ],
    version = 13,
    exportSchema = false
)
abstract class MonuDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun saveDao(): SaveDao
    abstract fun billDao(): BillDao
}