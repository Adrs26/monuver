package com.android.monu.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.monu.R
import com.android.monu.presentation.components.DebouncedIconButton
import com.android.monu.ui.theme.interFontFamily
import java.util.Calendar

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
            text = stringResource(greetingText()),
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
                .background(color = Color.White, shape = CircleShape)
                .padding(6.dp),
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

private fun greetingText(): Int {
    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    return when (hourOfDay) {
        in 6..10 -> R.string.good_morning
        in 11..14 -> R.string.good_afternoon
        in 15..17 -> R.string.good_evening
        else -> R.string.good_night
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    HomeTopBar(
        toSettings = {}
    )
}