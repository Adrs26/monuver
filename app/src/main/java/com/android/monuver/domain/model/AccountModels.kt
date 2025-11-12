package com.android.monuver.domain.model

data class AccountState(
    val id: Int = 0,
    val name: String,
    val type: Int,
    val balance: Long,
    val isActive: Boolean
)

data class AddAccountState(
    val name: String,
    val type: Int,
    val balance: Long
)

data class EditAccountState(
    val id: Int,
    val name: String,
    val type: Int,
    val balance: Long
)