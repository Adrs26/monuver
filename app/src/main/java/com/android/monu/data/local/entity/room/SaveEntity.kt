package com.android.monu.data.local.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving")
data class SaveEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val targetDate: String,
    val targetAmount: Long,
    val currentAmount: Long,
    val isActive: Boolean,
)
