package com.android.monu.ui.feature.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersHolder

fun String.showMessageWithToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

@Composable
fun Modifier.debouncedClickable(
    debounceTime: Long = 700L,
    onClick: () -> Unit
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    return this.then(
        Modifier.clickable {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= debounceTime) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController,
    noinline parameters: (() -> ParametersHolder)? = null
): T {
    val navGraphRoute = destination.parent?.route ?: error("No parent route")
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return if (parameters != null) {
        koinViewModel(viewModelStoreOwner = parentEntry, parameters = parameters)
    } else {
        koinViewModel(viewModelStoreOwner = parentEntry)
    }
}