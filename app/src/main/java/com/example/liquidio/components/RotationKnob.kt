package com.example.liquidio.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2

@Composable
fun RotationKnob(
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .size(250.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val dragPoint = change.position
                    val center = Offset(size.width / 2f, size.height / 2f)

                    // Calculate angle correctly
                    // We use (y - center.y) and (x - center.x)
                    val angleInRad = atan2(dragPoint.y - center.y, dragPoint.x - center.x)
                    var angleInDeg = angleInRad * (180f / PI.toFloat())

                    // Adjust so that 0 degrees is "Right" and it rotates smoothly
                    if (angleInDeg < 0) angleInDeg += 360f

                    rotationAngle = angleInDeg

                    // Map 0-360 degrees to 0-600 value
                    val value = ((rotationAngle / 360f) * 600).toInt()
                    onValueChange(value)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(rotationZ = rotationAngle) // This rotates the whole canvas
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2
            val strokeWidth = 15.dp.toPx()

            // 1. Draw Knob Body
            drawCircle(
                color = Color.DarkGray,
                radius = radius - strokeWidth
            )

            // 2. Draw Red Marker (The "Click Here" area)
            // This draws a red arc on the outer edge
            drawArc(
                color = Color.Red,
                startAngle = -20f, // Centered around the pointer
                sweepAngle = 40f,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 3. Draw the small indicator circle (Cyan)
            drawCircle(
                color = Color.Cyan,
                radius = radius * 0.15f,
                center = Offset(center.x + radius * 0.65f, center.y)
            )
        }
    }
}