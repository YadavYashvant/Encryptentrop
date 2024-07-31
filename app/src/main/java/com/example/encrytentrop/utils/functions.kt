package com.example.encrytentrop.utils

fun encryptText(text: String, colors: List<Int>): String {
    val entropy = calculateEntropy(colors)
    val key = generateKeyFromEntropyColorsAndRandomness(entropy, colors)
    return text.mapIndexed { index, char ->
        char.code.xor(key[index % key.length].code).toChar()
    }.joinToString("")
}

fun decryptText(encryptedText: String, colors: List<Int>): String {
    val entropy = calculateEntropy(colors)
    val key = generateKeyFromEntropyColorsAndRandomness(entropy, colors)
    return encryptedText.mapIndexed { index, char ->
        char.code.xor(key[index % key.length].code).toChar()
    }.joinToString("")
}