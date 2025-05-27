package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.ui.theme.SoftGrey
import com.android.monu.ui.theme.interFontFamily
import com.android.monu.utils.extensions.debouncedClickable

@Composable
fun HomeMenuButtonBar(
    modifier: Modifier = Modifier,
    navigateToBudgeting: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeMenuButton(
            icon = R.drawable.ic_donut,
            title = stringResource(R.string.budgeting),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f),
            onClick = navigateToBudgeting
        )
        HomeMenuButton(
            icon = R.drawable.ic_receipt_long,
            title = stringResource(R.string.bills),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f),
            onClick = { }
        )
        HomeMenuButton(
            icon = R.drawable.ic_flag,
            title = stringResource(R.string.goals),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f),
            onClick = { }
        )
        HomeMenuButton(
            icon = R.drawable.ic_currency_exchange,
            title = stringResource(R.string.currency),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f),
            onClick = { }
        )
    }
}

@Composable
fun HomeMenuButton(
    icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = SoftGrey, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
            .debouncedClickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Black
        )
        Text(
            text = title,
            modifier = Modifier.padding(top = 8.dp),
            style = TextStyle(
                fontSize = 10.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeMenuButtonBarPreview() {
    HomeMenuButtonBar(
        navigateToBudgeting = { }
    )
}
