package com.vkochenkov.equationsolver

import kotlin.math.sqrt

//todo пример
//var tex = "This come from string. You can insert inline formula:" +
//        " \\(ax^2 + bx + c = 0\\) " +
//        "or displayed formula: $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$"

const val DISCRIMINANT_FORMULA: String = "$$\\ D = b^2-4ac $$"
const val QUADRATIC_X12_FORMULA: String = "$$\\ x_{1,2} = {-b \\pm \\sqrt{D} \\over 2a}$$"
const val QUADRATIC_X_FORMULA: String = "$$\\ x = {-b \\over 2a}$$"
const val LINEAR_X_FORMULA: String = "$$\\ x = {-c \\over b}$$"
const val EQUATION_PATTERN : String = "$$\\ ax^2+bx+c $$"

class QuadraticEquation(
    val aPair: Pair<String, String>,
    val bPair: Pair<String, String>,
    val cPair: Pair<String, String>
) {

    private val a = glueSignWithNumber(aPair)
    private val b = glueSignWithNumber(bPair)
    private val c = glueSignWithNumber(cPair)
    private var isQuardEq = a!=0f

    private val d: Float = (this.b * this.b) - (4 * this.a * this.c)

    //todo дать ограничение на длину чисел
    private val quadrX1: Float = (changeSign(b) + (sqrt(d))) / (2 * a)
    private val quadrX2: Float = (changeSign(b) - (sqrt(d))) / (2 * a)
    private val linearX: Float = (changeSign(c))/b

    override fun toString(): String {
        var str = showBasicEquation()
        str += if (isQuardEq) showSolutionForQuadraticEquation() else showSolutionForLinearEquation()
        return str
    }

    private fun showBasicEquation(): String {
        //todo убрать отображение всех х с 0 индексами и выводить индексы отдельно
        var str = "Ваше уравнение: $$\\ ${a}x^2${checkForAddSign(b)}x${checkForAddSign(c)}=0 $$ "
        return str
    }

    private fun showSolutionForQuadraticEquation(): String {
        var str = "Решение через формулу дискриминанта:"
        str += DISCRIMINANT_FORMULA
        str += "$$\\ D = ${checkForAddParentheses(b)}^2- 4 * ${checkForAddParentheses(a)} * ${checkForAddParentheses(c)} $$ "
        str += "$$\\ D = ${d} $$"
        if (d > 0) {
            str += QUADRATIC_X12_FORMULA
            str += "$$\\ x_1 = {${changeSign(b)} + \\sqrt{${d}} \\over 2*${checkForAddParentheses(a)}}$$"
            str += "$$\\ x_2 = {${changeSign(b)} - \\sqrt{${d}} \\over 2*${checkForAddParentheses(a)}}$$"
            str += "Ответ: "
            str += "$$\\ x_1 = ${quadrX1}; x_2 = ${quadrX2}$$"
        } else if (d==0f) {
            str += QUADRATIC_X_FORMULA
            str += "$$\\ x = {${changeSign(b)} \\over 2*${checkForAddParentheses(a)}}$$"
            str += "Ответ: "
            str += "$$\\ x = ${quadrX1}$$"
        } else {
            str += "Ответ: "
            str += "Уравнение не имеет решений для натуральных корней"
        }
        return str
    }

    private fun showSolutionForLinearEquation(): String {
        //todo нужно обработать ответы с нан и инфинити
        var str = "Решение: "
        str += LINEAR_X_FORMULA
        str += "$$\\ x = {${changeSign(c)} \\over ${b}}$$"
        str += "Ответ: "
        str += "$$\\ x = ${linearX} $$"
        return str
    }

    private fun glueSignWithNumber(pair: Pair<String, String>): Float {
        val returnValue = (pair.first + pair.second).toFloat()
        return if (returnValue!=-0f) returnValue else 0f
    }

    private fun checkForAddParentheses(number: Float): String {
        return if (number>=0) {
            number.toString()
        } else {
            "(${number.toString()})"
        }
    }

    private fun checkForAddSign(number: Float): String {
        return if (number>=0) {
            "+${number.toString()}"
        } else {
            number.toString()
        }
    }

    private fun changeSign(number: Float): Float {
        return if (number==0f || number==-0f) 0f else number*(-1)
    }
}