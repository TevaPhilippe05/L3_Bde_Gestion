package com.example.bdeorga.activity.functions

fun tronquerText(text: String, maxLength: Int): String {
    return if (text.length > maxLength) text.take(maxLength) + " …" else text
}
