package com.android.monu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val type: Int,
    val category: Int,
    val date: String,
    val month: Int,
    val year: Int,
    val amount: Long,
    val budgetingId: Long? = null,
    val budgetingTitle: String? = null
)
