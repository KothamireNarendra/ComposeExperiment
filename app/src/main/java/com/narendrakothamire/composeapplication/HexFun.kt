package com.narendrakothamire.composeapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import java.lang.Math.cos
import java.lang.Math.sin

private const val NUMS = 6
private const val BASE_ANG = 60.0

@Composable
fun HexFun(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val t = animatedProgress.value
    Canvas(modifier = modifier) {
        val s = size.width * .1388f
        translate(size.width / 2, size.height / 2) {

            val temp = 1f / 8f
            val step: Int = (t / temp).toInt()

            var count = step
            val q = map(t, (temp) * step, (temp) * (step + 1), 0f, 1f)
            if (step == 3 || step == 6) {
                drawInner(q, s)
            } else {
                if (step > 6) {
                    count -= 2
                } else if (step > 3) {
                    count--
                }
                drawOuter(s)

                for (i in 0 .. NUMS-1) {
                    rotate((60f * i), Offset(0f, 0f)) {
                        translate(-s * 2, 0f) {
                            for (j in 0 .. NUMS-1) {
                                val x: Float = (s * cos(Math.toRadians(BASE_ANG * j))).toFloat()
                                val y: Float = (s * sin(Math.toRadians(BASE_ANG * j))).toFloat()
                                if (j == 0) {
                                    path.reset()
                                    path.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                    if (j == 5) path.close()
                                }
                            }

                            rotate((BASE_ANG * easing1(q)).toFloat(), Offset(s, 0f)) {
                                rotate((BASE_ANG * (count - 1)).toFloat(), Offset(s, 0f)) {
                                    drawPath(path, color = Color.Red, style = Stroke(5f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun DrawScope.drawInner(q: Float, s: Float) {
    val w = s * 2 * cos(Math.toRadians(BASE_ANG/2)).toFloat()
    drawOuter(2 * s)
    rotate((BASE_ANG * easing1(q)).toFloat(), Offset(0f, 0f)) {
        for (j in 0..1) {
            rotate(90 + 180f * j, Offset(0f, 0f)) {
                val ang = (2 * Math.PI) / 3
                for (i in 0 .. 2) {
                    val x = w * cos(i * ang).toFloat()
                    val y = w * sin(i * ang).toFloat()
                    if (i == 0) {
                        path.reset()
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                        if (i == 2) {
                            path.close()
                        }
                    }
                    drawPath(path, color = Color.Red, style = Stroke(5f))
                }
            }
        }

        for (j in 0 .. NUMS-1) {
            val x: Float = (s * cos(Math.toRadians(BASE_ANG * j))).toFloat()
            val y: Float = (s * sin(Math.toRadians(BASE_ANG * j))).toFloat()

            path.reset()
            path.moveTo(0f, 0f)
            path.lineTo(x, y)

            drawPath(path, color = Color.Red, style = Stroke(5f))
        }
    }
}

fun DrawScope.drawOuter(r: Float) {
    for (j in 0 .. NUMS-1) {
        val x: Float = (r * cos(Math.toRadians(BASE_ANG * j))).toFloat()
        val y: Float = (r * sin(Math.toRadians(BASE_ANG * j))).toFloat()
        if (j == 0) {
            path.reset()
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
            if (j == 5) path.close()
        }

        drawPath(path, color = Color.Red, style = Stroke(5f))
    }
}