package com.android.monuver.feature.account.domain.model

internal data class AddAccountState(
    val name: String,
    val type: Int,
    val balance: Long
)