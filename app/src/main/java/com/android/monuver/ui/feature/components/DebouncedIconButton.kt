package com.android.monuver.ui.feature.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DebouncedIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    debounceTime: Long = 700L,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    IconButton(
        onClick = {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= debounceTime) {
                lastClickTime = currentTime
                onClick()
            }
        },
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        content = content
    )
}