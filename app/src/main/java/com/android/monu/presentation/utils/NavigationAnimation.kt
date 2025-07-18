package com.android.monu.presentation.utils

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object NavigationAnimation {
    val enter = slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
    val exit = slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
    val popEnter = slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
    val popExit = slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()
}