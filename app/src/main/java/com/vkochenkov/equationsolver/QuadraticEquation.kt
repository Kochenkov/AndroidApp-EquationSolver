package com.vkochenkov.equationsolver

import kotlin.math.sqrt

//todo пример
//var tex = "This come from string. You can insert inline formula:" +
//        " \\(ax^2 + bx + c = 0\\) " +
//        "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$"

const val DISCRIMINANT_FORMULA: String = "$$\\ D = b^2-4ac $$"
const val X12_FORMULA: String = "$$\\ x_{1,2} = {-b \\pm \\sqrt{D} \\over 2a}$$"
const val X_FORMULA: String = "$$\\ x = {-b \\pm \\over 2a}$$"
const val EQUATION_PATTERN : String = "$$\\ ax^2+bx+c $$"

class QuadraticEquation(
    val aPair: Pair<String, String>,
    val bPair: Pair<String, String>,
    val cPair: Pair<String, String>
) {

    private val a = glueSignWithNumber(aPair)
    private val b = glueSignWithNumber(bPair)
    private val c = glueSignWithNumber(cPair)

    private val d: Float = this.b * this.b - 4 * this.a * this.c
    private val x1: Float = (-b - (sqrt(d))) / (2 * a)
    private val x2: Float = (-b + (sqrt(d))) / (2 * a)

    val howManyRealRoots: Int = if (d > 0) 2 else if (d.equals(0)) 1 else 0

    override fun toString(): String {
        var str = "\\\\mathcal{Ваше уравнение:}\\\n"//todo
        str += "\\(${a}x^2${checkAndAddPlusSign(b)}x${checkAndAddPlusSign(c)}=0\\)"
        str += "D = b² - 4ac \n"
        str += "D = ${b}² - 4 * ${checkAndAddParentheses(a)} * ${checkAndAddParentheses(c)} \n"
        str += "D = ${d} \n"
        if (d > 0) {
            str += "x₁,₂ = (-b ± √D)/(2a) \n"
            str += "x₁ = (-${b} - √${d})/(2*${a}) \n"
            str += "x₂ = (-${b} + √${d})/(2*${a}) \n"
            str += "x₁ = ${x1} \n"
            str += "x₂ = ${x2}"
        } else if (d.equals(0)){
            str += "x = -b/(2a) \n"
            str += "x = -${b}/(2*${a}) \n"
            str += "x = ${x1}"
        } else {
            str += "Уравнение не имеет натуральных корней"
        }
        return str
    }

    private fun glueSignWithNumber(pair: Pair<String, String>): Float {
        val sign = pair.first
        val number = pair.second
        return (sign + number).toFloat()
    }

    private fun checkAndAddParentheses(number: Float): String {
        return if (number>=0) {
            number.toString()
        } else {
            "(${number.toString()})"
        }
    }

    private fun checkAndAddPlusSign(number: Float): String {
        return if (number>=0) {
            "+${number.toString()}"
        } else {
            number.toString()
        }
    }
}