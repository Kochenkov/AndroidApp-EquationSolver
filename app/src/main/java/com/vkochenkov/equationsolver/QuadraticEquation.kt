package com.vkochenkov.equationsolver

class QuadraticEquation(var a: Float, var b: Float, var c: Float) {
    //ax2+bx+c=0
    val discriminant: Float
        get() {
            return (this.b * this.b - 4 * this.a * this.c)
        }
}