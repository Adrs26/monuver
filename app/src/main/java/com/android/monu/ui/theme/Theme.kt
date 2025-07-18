package com.android.monu.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = SoftWhite,
    background = SoftWhite,
    onBackground = Color.Black,
    surface = GrayBlue,
    onSurface = Color.Black,
    surfaceVariant = Color.LightGray,
    onSurfaceVariant = Color.Gray,
    secondaryContainer = BluePrimary,
    onSecondaryContainer = SoftWhite
)

private val Shape = Shapes(
    extraSmall = CircleShape,
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp)
)

@Composable
fun MonuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shape,
        content = content
    )
}