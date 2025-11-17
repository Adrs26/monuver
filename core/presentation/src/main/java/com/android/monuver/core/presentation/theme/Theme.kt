package com.android.monuver.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.android.monuver.core.domain.common.ThemeState

@Composable
fun MonuTheme(
    themeState: ThemeState,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeState) {
        ThemeState.Light -> false
        ThemeState.Dark -> true
        else -> isSystemInDarkTheme()
    }

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