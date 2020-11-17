package com.narendrakothamire.composeapplication

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.HorizontalGradient
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

const val NUM_ARC = 16

private val colorMap = mutableMapOf<String, ShaderBrush>()

private val color1 = Color(82, 172, 255)
private val color2 = Color(189, 247, 247)
private val color3 = Color(255, 227, 44)

@Composable
fun Worms(modifier: Modifier = Modifier) {

    val animatedProgress = animatedFloat(0f)
    onActive {
        animatedProgress.animateTo(
            targetValue = 1f,
            anim = repeatable(
                iterations = AnimationConstants.Infinite,
                animation = tween(durationMillis = 3000, easing = LinearEasing),
            ),
        )
    }
    val t = 1 - animatedProgress.value
    var flip = false

    Canvas(modifier = modifier) {
        val a = size.width / 8 + 10

        for (it in 0 until NUM_ARC) {
            val offset = (a - a / 1.414)
            //val offset = 0.2927 * radius

            val r = it / 4
            val c = it % 4

            flip = if (c == 0) {
                r % 2 == 0
            } else {
                !flip
            }

            val angle = if (flip) 360 * t else -(360 * t + 100)

            val x = a + 20 + (a * 2 * r) - offset.toFloat() * r
            val y = a + 20 + (a * 2 * c) - offset.toFloat() * c


            rotate(angle, pivot = Offset(x, y)) {
                drawArc(brush = getGradient(x, a, true), 55f, 80f, false, Offset(x - a, y - a), size = Size((2 * a), (2 * a)), style = Stroke(width = 10f))
                drawArc(brush = getGradient(x, a, false), 235f, 80f, false, Offset(x - a, y - a), size = Size((2 * a), (2 * a)), style = Stroke(width = 10f))
            }
        }
    }
}

private fun getGradient(x: Float, w: Float, isTop: Boolean): ShaderBrush{
    val list = if(isTop){
        listOf(
            color1,
            color1,
            color1,
            color2,
            color2,
            color3,
            color3,
            color3
        )
    }else{
        listOf(
            color3,
            color3,
            color3,
            color2,
            color2,
            color1,
            color1,
            color1
        )
    }
    return colorMap.getOrPut("$x-$isTop"){
        HorizontalGradient(
            list,
            x - w, x + w
        )
    }
}