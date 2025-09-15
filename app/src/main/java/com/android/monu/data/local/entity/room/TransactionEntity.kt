package com.android.monu.data.local.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val type: Int,
    val parentCategory: Int,
    val childCategory: Int,
    val date: String,
    val month: Int,
    val year: Int,
    val timeStamp: Long,
    val amount: Long,
    val sourceId: Int,
    val sourceName: String,
    val destinationId: Int? = null,
    val destinationName: String? = null,
    val saveId: Long? = null,
    val billId: Long? = null,
    val isLocked: Boolean,
    val isSpecialCase: Boolean
)