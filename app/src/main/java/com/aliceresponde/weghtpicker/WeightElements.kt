package com.aliceresponde.weghtpicker

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import java.lang.Math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Scale(
    modifier: Modifier = Modifier,
    style: ScaleStyle = ScaleStyle(),
    minWeight: Int = 20,
    maxWeight: Int = 250,
    initialWeight: Int = 80,
    onWeightChange: (Int) -> Unit
) {
    val radius = style.radius
    val scaleWidth = style.scaleWidth
    var center by remember { mutableStateOf(Offset.Zero) }
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    var angle by remember { mutableFloatStateOf(0f) }

    val textMeasurer = rememberTextMeasurer()
    var dragStartAngle by remember { mutableFloatStateOf(0f) }
    val oldAngle = remember { mutableFloatStateOf(angle) }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = {offset ->
                        dragStartAngle = -atan2(
                            y = circleCenter.x - offset.x,
                            x = circleCenter.y - offset.y
                        ) * (180f / PI.toFloat())

                    },
                    onDragEnd = {
                        oldAngle.floatValue = angle
                    }
                ) { change, _ ->
                    val touchAngel  = -atan2(
                        y = circleCenter.x - change.position.x,
                        x = circleCenter.y - change.position.y
                    ) * (180f / PI.toFloat())
                    val newAngle = oldAngle.floatValue + (touchAngel - dragStartAngle)
                    angle = newAngle.coerceIn(
                        minimumValue = initialWeight - maxWeight.toFloat(),
                        maximumValue = initialWeight - minWeight.toFloat()
                    )
                    onWeightChange(
                        initialWeight - angle.toInt()
                    )
                }
            }
    ) {
        center = this.center // center of the canvas
        this.size.height
        circleCenter = Offset(
            x = center.x,
            y = scaleWidth.toPx() / 2f + radius.toPx()
        )

        val outerRadius = radius.toPx() + scaleWidth.toPx() / 2f
        val innerRadius = radius.toPx() - scaleWidth.toPx() / 2f

        // scale canvas with not content
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    strokeWidth = scaleWidth.toPx()
                    color = Color.WHITE
                    setStyle(Paint.Style.STROKE)
                    setShadowLayer(
                        40f,
                        0f,
                        0f,
                        Color.argb(70f, 0f, 0f, 0f)
                    )
                }
            )
        }
        // 20<= i <= 250
        for (i in minWeight..maxWeight) {

            val angleInRad = (i - initialWeight + angle - 90) * (PI / 180f).toFloat()

            // draw lines per kg in the scale
            val lineType = when {
                i % 10 == 0 -> LineType.TEN_STEP
                i % 5 == 0 -> LineType.FIVE_STEP
                else -> LineType.NORMAL
            }

            val lineColor = when (lineType) {
                LineType.NORMAL -> style.normalLineColor
                LineType.FIVE_STEP -> style.fiveStepLineColor
                else -> style.tenStepLineColor
            }

            val lineLength = when (lineType) {
                LineType.NORMAL -> style.normalLineLength
                LineType.FIVE_STEP -> style.fiveStepLineLength
                else -> style.tenStepLineLength
            }

            val lineStart = Offset(
                x = (outerRadius - lineLength.toPx()) * cos(angleInRad) + circleCenter.x,
                y = (outerRadius - lineLength.toPx()) * sin(angleInRad) + circleCenter.y
            )

            val lineEnd = Offset(
                x = outerRadius * cos(angleInRad) + circleCenter.x,
                y = outerRadius * sin(angleInRad) + circleCenter.y
            )

            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

            // draw text for every 10 kh
            drawContext.canvas.nativeCanvas.apply {
                if (lineType == LineType.TEN_STEP) {
                    val textRadius =
                        outerRadius - lineLength.toPx() - 5.dp.toPx() - style.textSize.toPx()
                    val x = circleCenter.x + (textRadius * cos(angleInRad))
                    val y = circleCenter.y + (textRadius * sin(angleInRad))

                    withRotation(
                        degrees = (angleInRad * (180f / PI.toFloat())) + 90f,
                        pivotX = x,
                        pivotY = y
                    ) {
                        drawText(
                            (i).toString(),
                            x,
                            y,
                            Paint().apply {
                                textSize = style.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    } // end rotation body
                }
            }



            val middleTop = Offset(
                x = circleCenter.x,
                y = circleCenter.y - innerRadius - style.scaleIndicatorLength.toPx()
            )

            val bottomLeft = Offset(
                x = circleCenter.x - 4f,
                y = circleCenter.y - innerRadius
            )

            val bottomRight = Offset(
                x = circleCenter.x + 4f,
                y = circleCenter.y - innerRadius
            )

            val indicator = Path().apply {
                moveTo(middleTop.x, middleTop.y)
                lineTo(bottomLeft.x, bottomLeft.y)
                lineTo(bottomRight.x, bottomRight.y)
                lineTo(middleTop.x, middleTop.y)
            }

            drawPath(
                path = indicator,
                color = style.scaleIndicatorColor
            )
        }
    } // end canvas

}
