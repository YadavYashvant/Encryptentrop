package com.example.encrytentrop.utils

import kotlin.math.log2

fun calculateEntropy(colors: List<Int>): Double {
    val colorFrequency = colors.groupingBy { it }.eachCount()
    val totalColors = colors.size.toDouble()
    return colorFrequency.values.sumOf { frequency ->
        val probability = frequency / totalColors
        -probability * log2(probability)
    }
}

fun generateKeyFromEntropyAndColors(entropy: Double, colors: List<Int>): String {
    val colorString = colors.joinToString("") { it.toString() }
    val keyLength = (entropy * 10).toInt()
    return (1..keyLength).map { (it % 256).toChar() }.joinToString("") + colorString
}