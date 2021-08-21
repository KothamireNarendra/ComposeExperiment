package com.narendrakothamire.composeapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode


@Composable
fun SquareFun(modifier: Modifier = Modifier) {
    val path = remember { Path() }
    var isDone = false
    var aColor: AColor = AColor.WHITE
    val paint = remember { Paint() }

    val infiniteTransition = rememberInfiniteTransition()
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    Canvas(modifier = modifier) {
        val t = animatedProgress.value
        val w = size.width
        val h = size.height
        val d = w * 0.28f
        val centerX = w / 2
        val centerY = h / 2

        if (t < 0.5) {
            if (isDone) aColor = aColor.invert()
            isDone = false
            val tt = map(t, 0f, 0.5f, 0f, 1f)

            drawRect(
                color = aColor.getColor(),
                topLeft = Offset(
                    0f,
                    h / 2 - w / 2),
                size = Size(w, w)
            )

            val p = easing1(tt)
            val o = progress(p, w, d * 2)
            drawRect(
                color = aColor.invert().getColor(),
                topLeft = Offset(
                    progress(p, 0f, w / 2 - d),
                    progress(p, h / 2 - centerX, h / 2 - d)
                ),
                size = Size(o, o)
            )
            translate(centerX, centerY) {
                drawPaths(path, d, 1f, aColor.getColor())
            }
        } else {
            drawRect(
                color = aColor.getColor(),
                topLeft = Offset(
                    0f,
                    h / 2 - w / 2),
                size = Size(w, w)
            )

            val tt = map(t, 0.5f, 1f, 0f, 1f)
            drawIntoCanvas {canvas ->
                canvas.saveLayer(size.toRect(), paint)
                translate(centerX, centerY) {
                    drawPaths(path, d, tt, aColor.invert().getColor(), true)
                }
                canvas.restore()
            }

            isDone = true
        }
    }

}

val matrix = arrayOf(-1 to -1, 1 to -1, -1 to 1, 1 to 1)

fun DrawScope.drawPaths(path: Path, d: Float, t: Float, color: Color, shouldBlend: Boolean = false) {
    for (i in 0 until 4) {
        rotate(180 * easing2(t), pivot = Offset(
            (matrix[i].first * d / 2),
            (matrix[i].second * d / 2))
        ) {
            path.apply {
                path.reset()
                moveTo(d * matrix[i].first, 0f)
                lineTo(d * matrix[i].first, d * matrix[i].second)
                lineTo(0f, d * matrix[i].second)
                close()
            }
            val mode = if(shouldBlend) BlendMode.Xor else DefaultBlendMode
            drawPath(path = path, color = color, style = Fill, blendMode = mode)
        }
    }

}



fun progress(p: Float, start: Float, end: Float) = start + (end - start) * p

enum class AColor {
    WHITE,
    MAGENTA;

    fun invert(): AColor = if (this == WHITE) MAGENTA else WHITE

    fun getColor(): Color = if (this == WHITE) Color.White else Color(234,73,95)
}