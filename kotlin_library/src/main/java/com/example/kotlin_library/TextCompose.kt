package com.example.kotlin_library
fun <T : Number> Number.getSum(a: T, b: T): Float {
    return a.toFloat()+b.toFloat()
}

