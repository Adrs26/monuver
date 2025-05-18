package com.android.monu.ui.navigation

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavItem(
    val title: String,
    val filledIcon: Painter,
    val outlinedIcon: Painter,
    val screen: Screen
)