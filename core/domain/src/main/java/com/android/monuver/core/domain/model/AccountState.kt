package com.android.monuver.core.domain.model

data class AccountState(
    val id: Int = 0,
    val name: String,
    val type: Int,
    val balance: Long,
    val isActive: Boolean
)