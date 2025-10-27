package com.android.monu.ui.feature.screen.budgeting.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.monu.R
import com.android.monu.ui.feature.components.TextWithSwitch
import com.android.monu.utils.Cycle

@Composable
fun BudgetTextSwitchField(
    isOverflowAllowed: Boolean,
    onOverflowAllowedChange: (Boolean) -> Unit,
    isAutoUpdate: Boolean,
    onAutoUpdateChange: (Boolean) -> Unit,
    budgetCycle: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        TextWithSwitch(
            text = stringResource(R.string.budgeting_overflow_allowed),
            checked = isOverflowAllowed,
            onCheckedChange = onOverflowAllowedChange,
            isEnable = true
        )
        TextWithSwitch(
            text = stringResource(R.string.budgeting_auto_update),
            checked = isAutoUpdate,
            onCheckedChange = onAutoUpdateChange,
            isEnable = budgetCycle != Cycle.CUSTOM
        )
    }
}