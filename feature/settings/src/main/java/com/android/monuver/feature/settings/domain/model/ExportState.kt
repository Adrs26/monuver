package com.android.monuver.feature.settings.domain.model

internal data class ExportState(
    val title: String,
    val username: String,
    val startDate: String,
    val endDate: String,
    val sortType: Int,
    val isTransactionGrouped: Boolean,
    val isTransferIncluded: Boolean
)
