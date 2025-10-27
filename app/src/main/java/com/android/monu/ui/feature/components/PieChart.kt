package com.android.monu.ui.feature.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.android.monu.domain.model.TransactionCategorySummaryState
import com.android.monu.ui.feature.utils.DatabaseCodeMapper
import kotlinx.coroutines.launch

@Composable
fun PieChart(
    values: List<TransactionCategorySummaryState>,
    modifier: Modifier = Modifier
) {
    val numberOfGaps = values.size
    val remainingDegrees = 360 - (numberOfGaps * 12)
    val total = values.fold(0f) { acc, pie -> acc + pie.totalAmount }.div(remainingDegrees)
    var currentSum = 0f

    val arcs = values.mapIndexed { index, pieDataPoint ->
        val startAngle = currentSum + (index * 12)
        currentSum += pieDataPoint.totalAmount.div(total)
        ArcData(
            targetSweepAngle = pieDataPoint.totalAmount.div(total),
            animation = Animatable(0f),
            color = DatabaseCodeMapper.toParentCategoryIconBackground(pieDataPoint.parentCategory),
            startAngle = -90 + startAngle
        )
    }

    LaunchedEffect(key1 = arcs) {
        arcs.mapIndexed { index, arc ->
            launch {
                arc.animation.animateTo(
                    targetValue = arc.targetSweepAngle,
                    animationSpec = tween(
                        durationMillis = 150,
                        easing = LinearEasing,
                        delayMillis = index * 150
                    )
                )
            }
        }
    }

    Canvas(
        modifier = modifier.size(160.dp)
    ) {
        val stroke = Stroke(width = 35f, cap = StrokeCap.Round)
        arcs.reversed().map {
            drawArc(
                color = it.color,
                startAngle = it.startAngle,
                sweepAngle = it.animation.value,
                useCenter = false,
                style = stroke
            )
        }
    }
}

data class ArcData(
    val targetSweepAngle: Float,
    val animation: Animatable<Float, AnimationVector1D>,
    val color: Color,
    val startAngle: Float
)