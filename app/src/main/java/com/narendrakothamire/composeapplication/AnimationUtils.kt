package com.narendrakothamire.composeapplication

import kotlin.math.pow

fun easing1(x: Float): Float {
    return when {
        x == 0f -> {
            0f
        }
        x == 1f -> {
            1f
        }
        x < 0.5f -> {
            2.0.pow(20 * x - 10.0).toFloat() / 2
        }
        else -> {
            (2f - 2.0.pow(-20f * x + 10.0)).toFloat() / 2
        }
    }

}

fun easing2(x: Float): Float = if (x < 0.5) (8 * x * x * x * x) else (1 - (-2 * x + 2).toDouble().pow(4.0) / 2).toFloat()

fun map(value: Float, start1: Float, stop1: Float, start2: Float, stop2: Float): Float {
    return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
}