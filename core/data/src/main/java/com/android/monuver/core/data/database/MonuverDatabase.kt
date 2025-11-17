package com.android.monuver.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.monuver.core.data.database.dao.AccountDao
import com.android.monuver.core.data.database.dao.BillDao
import com.android.monuver.core.data.database.dao.BudgetDao
import com.android.monuver.core.data.database.dao.SavingDao
import com.android.monuver.core.data.database.dao.TransactionDao
import com.android.monuver.core.data.database.entity.room.AccountEntity
import com.android.monuver.core.data.database.entity.room.BillEntity
import com.android.monuver.core.data.database.entity.room.BudgetEntity
import com.android.monuver.core.data.database.entity.room.SavingEntity
import com.android.monuver.core.data.database.entity.room.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        BudgetEntity::class,
        SavingEntity::class,
        BillEntity::class
               ],
    version = 23,
    exportSchema = false
)
abstract class MonuverDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun saveDao(): SavingDao
    abstract fun billDao(): BillDao
}