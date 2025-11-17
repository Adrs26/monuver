package com.android.monuver.feature.account.domain.model

internal data class EditAccountState(
    val id: Int,
    val name: String,
    val type: Int,
    val balance: Long
)