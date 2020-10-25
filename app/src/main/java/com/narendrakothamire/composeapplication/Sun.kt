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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.cos
import kotlin.math.sin

const val NUM = 45
const val N = 14

@Composable
fun Sun(modifier: Modifier = Modifier) {

    val dataList = listOf(
        Data(.3f, .08f, 0f),
        Data(.3f, .15f, 0.33f),
        Data(.3f, .20f, 0.66f)
    )

    val animatedProgress = animatedFloat(0f)
    onActive {
        animatedProgress.animateTo(
            targetValue = 1f,
            anim = repeatable(
                iterations = AnimationConstants.Infinite,
                animation = tween(durationMillis = 4000, easing = LinearEasing),
            ),
        )
    }
    val t = 1 - animatedProgress.value
    Canvas(modifier = modifier) {
        val s = size.width
        translate(size.width / 2, size.height / 2) {
            for (i in 0 until N){
                rotate((360 * i / N).toFloat(), pivot = Offset(0f, 0f)){
                    dataList.forEach {
                        drawPath(s *it.f1 + s *it.f2, t + it.t)
                    }
                }
            }

        }
    }
}

private fun DrawScope.drawPath(rd: Float, q: Float) {
    for (i in 0 until NUM) {
        val value = i * 1.0f / NUM - 1
        val s = size.width
        val r = lerp(rd, rd + s *.26f, value)
        val w = map(cos(2 * Math.PI * value).toFloat(), 1f, -1f, 1f, 5f)
        val y = 0.040 * s * sin(2 * Math.PI * q - map(r, 0f, (NUM - 1).toFloat(), 0f, (Math.PI / 4).toFloat()))
        drawCircle(color = Color(233,73,94), center = Offset(r - w/2, (y - w/2).toFloat()), radius = w)
    }
}

private data class Data(val f1: Float, val f2: Float, val t: Float)



