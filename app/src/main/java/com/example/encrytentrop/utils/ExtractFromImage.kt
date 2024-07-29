package com.example.encrytentrop.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

fun extractColorsFromImage(bitmap: Bitmap): List<Int> {
    val palette = Palette.from(bitmap).generate()
    return palette.swatches.map { it.rgb }
}