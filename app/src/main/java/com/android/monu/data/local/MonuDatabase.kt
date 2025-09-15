package com.android.monu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.monu.data.local.dao.AccountDao
import com.android.monu.data.local.dao.BillDao
import com.android.monu.data.local.dao.BudgetDao
import com.android.monu.data.local.dao.SavingDao
import com.android.monu.data.local.dao.TransactionDao
import com.android.monu.data.local.entity.room.AccountEntity
import com.android.monu.data.local.entity.room.BillEntity
import com.android.monu.data.local.entity.room.BudgetEntity
import com.android.monu.data.local.entity.room.SavingEntity
import com.android.monu.data.local.entity.room.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        BudgetEntity::class,
        SavingEntity::class,
        BillEntity::class
               ],
    version = 20,
    exportSchema = false
)
abstract class MonuDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun budgetDao(): BudgetDao
    abstract fun saveDao(): SavingDao
    abstract fun billDao(): BillDao
}