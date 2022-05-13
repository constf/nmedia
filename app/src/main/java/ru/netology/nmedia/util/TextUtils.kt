package ru.netology.nmedia.util

fun convertToLiterals(n: Int): String {
    var s: String
    var num1: Int = n
    var num2: Int = num1

    var i: Int = 0
    while (num1 >= 1000) {
        i++
        num2 = num1
        num1 = num1 / 1000
    }

    s  = num1.toString()

    if (i == 0) return s

    if (num1 < 10) {
        val hundreds = (num2 % 1000) / 100
        if (hundreds > 0) {
            s += "."
            s += hundreds.toString()
        }
    }

    s += when(i){
        1 -> "K"
        2 -> "M"
        3 -> "B"
        4 -> "T"
        else -> "G" // Gooogol!
    }

    return s
}