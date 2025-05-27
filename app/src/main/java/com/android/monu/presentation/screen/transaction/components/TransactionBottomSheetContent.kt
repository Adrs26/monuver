package com.android.monu.presentation.screen.transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.theme.Green
import com.android.monu.ui.theme.Red
import com.android.monu.ui.theme.interFontFamily

@Composable
fun TransactionBottomSheetContent(
    modifier: Modifier = Modifier,
    navigateToAddIncome: () -> Unit,
    navigateToAddExpense: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        AddTransactionButton(
            background = Green,
            icon = R.drawable.ic_trending_up,
            title = stringResource(R.string.add_income),
            onClick = navigateToAddIncome
        )
        AddTransactionButton(
            background = Red,
            icon = R.drawable.ic_trending_down,
            title = stringResource(R.string.add_expense),
            onClick = navigateToAddExpense
        )
    }
}

@Composable
fun AddTransactionButton(
    background: Color,
    icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(color = background, shape = CircleShape)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.padding(4.dp),
                tint = Color.White,
            )
        }
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
    }
}