package com.android.monu.presentation.screen.budgeting.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Orange
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.NumberFormatHelper

@Composable
fun BudgetingListItem(
    title: String,
    startDate: String,
    endDate: String,
    amount: Long,
    maxAmount: Long,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White
) {
    Card(
        modifier = modifier
            .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { amount.toFloat() / maxAmount.toFloat() },
                    modifier = Modifier.size(64.dp),
                    color = changeProgressBarColor(amount, maxAmount),
                    strokeWidth = 5.dp,
                    trackColor = Color.LightGray,
                    gapSize = 2.dp
                )
                Text(
                    text = stringResource(
                        R.string.percentage_value,
                        NumberFormatHelper.formatToPercentageValue(amount, maxAmount)
                    ),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = changeProgressBarColor(amount, maxAmount)
                    )
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                )
                Text(
                    text = "$startDate - $endDate",
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = NumberFormatHelper.formatToConciseRupiah(amount),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(12.dp)
                            .padding(bottom = 3.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    Text(
                        text = NumberFormatHelper.formatToConciseRupiah(maxAmount),
                        modifier = Modifier.padding(bottom = 1.dp),
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

fun changeProgressBarColor(amount: Long, maxAmount: Long): Color {
    return when {
        amount.toDouble() / maxAmount.toDouble() > 0.9 -> Red
        amount.toDouble() / maxAmount.toDouble() > 0.6 -> Orange
        else -> Green
    }
}