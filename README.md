1. Create an scale for a range of kilograms with initial valur
2. Identify the lines for one kg, 5 kg, and 10 kg
3. create a circle with  big radius, and stroke with shadow  **useDrawLine**

       drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

4. **DrawText** using native canvas, with rotation

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
          }
   
5. use **onDragDetect** to handle the movement for the weight

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
    )


   ![image](https://github.com/user-attachments/assets/5f95d64f-0335-4a74-8a90-590a208be5a2)

   Its an interactive weght picket that looks like
   
   https://github.com/user-attachments/assets/087edb73-6e1d-4681-b047-f35a1eb51b46


