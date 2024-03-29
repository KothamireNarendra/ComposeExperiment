package com.narendrakothamire.composeapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val NUM_OF_SQUARES = 8

val path = Path()

@Composable
fun AnimSquare(modifier: Modifier = Modifier) {

    val infiniteTransition = rememberInfiniteTransition()
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    androidx.compose.material.Surface(color = Color.White) {


    Canvas(modifier = modifier) {
        val s = size.width * .30f
        translate(size.width / 2, size.height / 2) {
            val t = animatedProgress.value
            if (t <= 0.5) {
                val tt = map(t, 0f, 0.5f, 0f, 1f)
                rotate(45 * tt, Offset(0f, 0f)) {
                    for (i in 0 until NUM_OF_SQUARES) {
                        drawRect(i, tt, s)
                    }
                }
            } else {
                for (i in 0 until NUM_OF_SQUARES) {
                    drawRect(i, 0f, s)
                }
                drawCircle(color = Color.Red, radius = s, center = Offset(0f, 0f))
                val tt = map(t, 0.5f, 1f, 0f, 1f)
                rotate(45 + 45 * tt,  Offset(0f, 0f))  {
                    drawPaths(s)
                }

            }
        }
    }
    }
}

private fun DrawScope.drawRect(index: Int, t: Float, s: Float) {
    val a = s * sin(toRadians(22.5)) * 2

    val x = s * cos(toRadians(22.5)) * cos(toRadians((45 * index).toDouble()))
    val y = s * cos(toRadians(22.5)) * sin(toRadians((45 * index).toDouble()))

    val w = (a.toFloat() / sqrt(2f))
    withTransform({
        translate((-x).toFloat(), (-y).toFloat())
        rotate(-(45f * factor(index = index) + (90 * t)),  Offset(0f, 0f))
    }, {
        drawRect(
                color = Color.Red,
                topLeft = Offset(-w / 2f, -w / 2f),
                size = Size(w, w)
        )
    })
}


private fun DrawScope.drawPaths(s: Float) {
    val w =  (s - s * cos(toRadians(22.5))) + (s * sin(toRadians(22.5)))
    var xPos: Float? = null
    var yPos: Float? = null

    for (i in 0 until NUM_OF_SQUARES) {
        val x1 = s * cos(toRadians(22.5 + (45 * i).toDouble()))
        val y1 = s * sin(toRadians(22.5 + (45 * i).toDouble()))

        val x2 = (s - w) * cos(toRadians((45 + 45 * i).toDouble()))
        val y2 = (s - w) * sin(toRadians((45 + 45 * i).toDouble()))

        if (i == 0) {
            path.reset()
            path.lineTo(-x1.toFloat(), -y1.toFloat())
            path.lineTo(-x2.toFloat(), -y2.toFloat())
            xPos = -x1.toFloat()
            yPos = -y1.toFloat()
        } else {
            path.lineTo(-x1.toFloat(), -y1.toFloat())
            path.lineTo(-x2.toFloat(), -y2.toFloat())
            if (i == 7) {
                path.lineTo(xPos!!, yPos!!)
            }
        }
        drawPath(path, color = Color.White)
    }

}

private fun factor(index: Int) = if (index % 2 == 0) 1 else 0
