package com.android.monu.presentation.screen.reports.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.YearFilterButton
import com.android.monu.ui.theme.interFontFamily

@Composable
fun ReportsTopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.monthly_reports),
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
        YearFilterButton(year = "2025")
    }
}