package com.narendrakothamire.composeapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.graphicsLayer

private const val NUMS = 130
private val sqrs = mutableListOf<Sqr>()
private val overlap = (NUMS * 0.1).toInt()

private data class Sqr(val x: Float, val y: Float, var r: Float)

@Composable
fun Endless(modifier: Modifier = Modifier) {

    val paint = remember { Paint() }
    val infiniteTransition = rememberInfiniteTransition()
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val t = animatedProgress.value
    androidx.compose.material.Surface(color = Color.White) {


    Canvas(
        Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)) {
        val width = size.width
        val height = size.height
        val radius = width * 0.5
        val layerSize = radius * 0.25f
        t
        if (sqrs.isEmpty()) {
            repeat(NUMS) { index ->
                sqrs.add(
                    Sqr(
                        x = (width / 2 + (Math.sin((index.toFloat() / NUMS.toFloat()) * 2 * Math.PI) * (radius - layerSize))).toFloat(),
                        y = (height / 2 + (Math.cos((index.toFloat() / NUMS.toFloat()) * 2 * Math.PI) * (radius - layerSize))).toFloat(),
                        r = (index / NUMS * Math.PI * 2).toFloat()
                    )
                )
            }
        }

        repeat(NUMS) { index ->
            sqrs[index].r += 0.02f
        }

        drawIntoCanvas { canvas ->
            paint.blendMode = BlendMode.DstOver
            val bounds = size.toRect()
            canvas.saveLayer(bounds, paint)
            for (it in 0 until sqrs.size - overlap) {
                translate(sqrs[it].x, sqrs[it].y) {
                    rotate(Math.toDegrees(sqrs[it].r.toDouble()).toFloat(), Offset(0f, 0f)) {
                        drawRect(
                            color = Color(41,54,59),
                            topLeft = Offset(
                                ((-layerSize / 2).toFloat()),
                                ((-layerSize / 2f).toFloat())
                            ),
                            size = Size(layerSize.toFloat(), layerSize.toFloat()),
                            style = Stroke(5f),
                        )
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(
                                (-layerSize / 2 + 5 / 2).toFloat(),
                                (-layerSize / 2f + 5 / 2).toFloat()
                            ),
                            size = Size(layerSize.toFloat() - 5, layerSize.toFloat() - 5),
                            style = Fill,
                        )
                    }
                }
            }
            canvas.restore()

            paint.blendMode = BlendMode.DstIn
            canvas.saveLayer(bounds, paint)
            translate(sqrs[0].x, sqrs[0].y) {
                rotate(Math.toDegrees(sqrs[0].r.toDouble()).toFloat(), Offset(0f, 0f)) {
                    val s = layerSize +10
                    drawRect(
                        color = Color.Blue,
                        topLeft = Offset(
                            (-s / 2).toFloat(),
                            (-s / 2).toFloat()
                        ),
                        size = Size(s.toFloat(), s.toFloat()),
                        style = Fill,
                    )
                }
            }
            canvas.restore()

            paint.blendMode = BlendMode.DstOver
            canvas.saveLayer(bounds, paint)
            for (it in 0 until sqrs.size) {
                translate(sqrs[it].x, sqrs[it].y) {
                    rotate(
                        Math.toDegrees(sqrs[it].r.toDouble()).toFloat(),
                        Offset(0f, 0f)
                    ) {
                        drawRect(
                            color = Color(41,54,59),
                            topLeft = Offset(
                                ((-layerSize / 2).toFloat()),
                                ((-layerSize / 2f).toFloat())
                            ),
                            size = Size(layerSize.toFloat(), layerSize.toFloat()),
                            style = Stroke(5f),
                        )
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(
                                (-layerSize / 2 + 5 / 2).toFloat(),
                                (-layerSize / 2f + 5 / 2).toFloat()
                            ),
                            size = Size(layerSize.toFloat() - 5, layerSize.toFloat() - 5),
                            style = Fill,
                        )
                    }
                }
            }
            canvas.restore()
        }
    }
    }
}

