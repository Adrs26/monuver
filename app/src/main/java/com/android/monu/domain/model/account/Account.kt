package com.android.monu.domain.model.account

data class Account(
    val id: Int,
    val name: String,
    val type: Int,
    val balance: Long
)
