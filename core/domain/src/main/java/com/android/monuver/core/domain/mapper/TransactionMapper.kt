package com.android.monuver.core.domain.mapper

import com.android.monuver.core.domain.model.TransactionListItemState
import com.android.monuver.core.domain.model.TransactionState

fun TransactionState.toListItemState() = TransactionListItemState(
    id = id,
    title = title,
    type = type,
    parentCategory = parentCategory,
    childCategory = childCategory,
    date = date,
    amount = amount,
    sourceName = sourceName,
    isLocked = isLocked
)