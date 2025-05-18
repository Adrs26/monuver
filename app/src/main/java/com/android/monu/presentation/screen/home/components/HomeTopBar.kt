package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.DebouncedIconButton
import com.android.monu.ui.theme.interFontFamily

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    toSettings: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GreetingText(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        SettingsIconButton(toSettings = toSettings)
    }
}

@Composable
fun GreetingText(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Good Morning,",
            modifier = Modifier.padding(bottom = 2.dp),
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )
        Text(
            text = "Adrian Septiyadi",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )
    }
}

@Composable
fun SettingsIconButton(
    modifier: Modifier = Modifier,
    toSettings: () -> Unit
) {
    DebouncedIconButton(
        onClick = toSettings,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    HomeTopBar(
        toSettings = {}
    )
}