package com.android.monu.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

fun Long.toHighestRangeValue(): Long {
    return when (this) {
        in 0L..50_000L -> 50_000L
        in 50_000L..200_000L -> 200_000L
        in 200_000L..500_000L -> 500_000L
        in 500_000L..1_000_000L -> 1_000_000L
        in 1_000_000L..3_000_000L -> 3_000_000L
        in 3_000_000L..5_000_000L -> 5_000_000L
        in 5_000_000L..7_000_000L -> 7_000_000L
        in 7_000_000L..10_000_000L -> 10_000_000L
        in 10_000_000L..15_000_000L -> 15_000_000L
        in 15_000_000L..20_000_000L -> 20_000_000L
        in 20_000_000L..30_000_000L -> 30_000_000L
        in 30_000_000L..50_000_000L -> 50_000_000L
        in 50_000_000L..100_000_000L -> 100_000_000L
        in 100_000_000L..200_000_000L -> 200_000_000L
        in 200_000_000L..500_000_000L -> 500_000_000L
        else -> 1_000_000_000L
    }
}

fun String.showMessageWithToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

@Composable
fun Modifier.debouncedClickable(
    debounceTime: Long = 700L,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return this.then(
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = indication
        ) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= debounceTime) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}