package com.android.monuver.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun CommonLottieAnimation(
    lottieAnimation: Int,
    modifier: Modifier = Modifier,
    iterations: Int = 1
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieAnimation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize(0.6f)
        )
    }
}