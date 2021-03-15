package com.vkochenkov.equationsolver.entity

import android.content.Context
import com.vkochenkov.equationsolver.R
import kotlin.math.roundToInt
import kotlin.math.sqrt

const val DISCRIMINANT_FORMULA: String = "$$\\ D = b^2-4ac $$"
const val QUADRATIC_X12_FORMULA: String = "$$\\ x_{1,2} = {-b \\pm \\sqrt{D} \\over 2a}$$"
const val QUADRATIC_X_FORMULA: String = "$$\\ x = {-b \\over 2a}$$"
const val LINEAR_X_FORMULA: String = "$$\\ x = {-c \\over b}$$"
const val EQUATION_PATTERN: String = "$$\\ ax^2+bx+c $$"

enum class GraphicType {
    QUADRATIC, LINEAR, NO_GRAPHIC
}

class QuadraticEquation(
    val aPair: Pair<String, String>,
    val bPair: Pair<String, String>,
    val cPair: Pair<String, String>,
    val context: Context
) {
    /**
     * тип графика. По-дефолту - квадратное
     */
    var graphicType: GraphicType = GraphicType.QUADRATIC

    /**
     * коэффициенты уравнения
     */
    val a = glueSignWithNumber(aPair)
    val b = glueSignWithNumber(bPair)
    val c = glueSignWithNumber(cPair)

    /**
     * дискриминант
     */
    private val d: Float = (this.b * this.b) - (4 * this.a * this.c)

    /**
     * точки квадратного уравнения
     */
    val quadrX1 = negativeZeroValidation((changeSign(b) + (sqrt(d))) / (2 * a))
    val quadrX2 = negativeZeroValidation((changeSign(b) - (sqrt(d))) / (2 * a))
    val quadrX0 = negativeZeroValidation(-b / (2 * a))
    val quadrY0 = negativeZeroValidation(a * quadrX0 * quadrX0 + b * quadrX0 + c)

    /**
     * точки линейного уравнения
     */
    val linearX = negativeZeroValidation((changeSign(c)) / b)
    val linearY = negativeZeroValidation(b * linearX + c)

    override fun toString(): String {
        var str = showBasicEquation()
        str += showCoefficients()
        str += if (a != 0f) showSolutionForQuadraticEquation() else if (b != 0f) showSolutionForLinearEquation() else showError()
        return str
    }

    fun showZeroQuadraticRoot(): String {
        return "$$\\ x_0 = {${deleteZeroFromEnd(round(quadrX0))}}; y_0 = {${deleteZeroFromEnd(
            round(
                quadrY0
            )
        )}} $$"
    }

    private fun round(number: Float): Float {
        val i = 1000
        return (number * i).roundToInt().toFloat() / i
    }

    private fun deleteZeroFromEnd(number: Float): String {
        return deleteZeroFromEnd(number.toString())
    }

    private fun deleteZeroFromEnd(str: String): String {
        return if (str.endsWith(".0")) {
            str.replace(".0", "")
        } else {
            str
        }
    }

    private fun showBasicEquation(): String {
        var str = context.getString(R.string.your_eq)
        var eq = when (a) {
            0f -> ""
            1f -> "x^2"
            -1f -> "-x^2"
            else -> "${deleteZeroFromEnd(a)}x^2"
        }
        eq += when (b) {
            0f -> ""
            1f -> {
                if (eq != "") "+x" else "x"
            }
            -1f -> "-x"
            else -> {
                if (eq != "") "${deleteZeroFromEnd(checkForAddPlusSign(b))}x" else "${deleteZeroFromEnd(
                    b
                )}x"
            }
        }
        eq += when (c) {
            0f -> ""
            else -> {
                if (eq != "") deleteZeroFromEnd(checkForAddPlusSign(c)) else deleteZeroFromEnd(c)
            }
        }
        if (eq == "") {
            eq += "0"
        }
        eq += "=0"
        str += "$$\\ $eq $$"
        return str
    }

    private fun showCoefficients(): String {
        return "$$\\ a=${deleteZeroFromEnd(a)}; b=${deleteZeroFromEnd(b)}; c=${deleteZeroFromEnd(c)} $$"
    }

    private fun showSolutionForQuadraticEquation(): String {
        graphicType = GraphicType.QUADRATIC
        var str = context.getString(R.string.solution_discrim)
        str += DISCRIMINANT_FORMULA
        str += "$$\\ D = ${checkForAddParentheses(deleteZeroFromEnd(b))}^2 - 4 * ${checkForAddParentheses(
            deleteZeroFromEnd(a)
        )} * ${checkForAddParentheses(deleteZeroFromEnd(c))} $$ "
        str += "$$\\ D = ${deleteZeroFromEnd(round(d))} $$"
        if (d > 0) {
            str += QUADRATIC_X12_FORMULA
            str += "$$\\ x_1 = {${deleteZeroFromEnd(changeSign(b))} + \\sqrt{${deleteZeroFromEnd(
                round(d)
            )}} \\over 2*${checkForAddParentheses(deleteZeroFromEnd(a))}}$$"
            str += "$$\\ x_2 = {${deleteZeroFromEnd(changeSign(b))} - \\sqrt{${deleteZeroFromEnd(
                round(d)
            )}} \\over 2*${checkForAddParentheses(deleteZeroFromEnd(a))}}$$"
            str += context.getString(R.string.answer)
            str += "$$\\ x_1 = ${deleteZeroFromEnd(round(quadrX1))}; x_2 = ${deleteZeroFromEnd(
                round(
                    quadrX2
                )
            )}$$"
        } else if (d == 0f) {
            str += QUADRATIC_X_FORMULA
            str += "$$\\ x = {${deleteZeroFromEnd(changeSign(b))} \\over 2*${checkForAddParentheses(
                deleteZeroFromEnd(a)
            )}}$$"
            str += context.getString(R.string.answer)
            str += "$$\\ x = ${deleteZeroFromEnd(round(quadrX1))}$$"
        } else {
            graphicType = GraphicType.NO_GRAPHIC
            str += context.getString(R.string.answer) + " "
            str += context.getString(R.string.no_natural_solution).toString()
        }
        return str
    }

    private fun showSolutionForLinearEquation(): String {
        graphicType = GraphicType.LINEAR
        var str = context.getString(R.string.solution)
        str += LINEAR_X_FORMULA
        str += "$$\\ x = {${deleteZeroFromEnd(changeSign(c))} \\over ${deleteZeroFromEnd(b)}}$$"
        str += context.getString(R.string.answer)
        str += "$$\\ x = ${deleteZeroFromEnd(round(linearX))} $$"
        return str
    }

    private fun showError(): String {
        graphicType = GraphicType.NO_GRAPHIC
        return context.getString(R.string.wrong_answer)
    }

    private fun glueSignWithNumber(pair: Pair<String, String>): Float {
        val returnValue = (pair.first + pair.second).toFloat()
        return if (returnValue != -0f) returnValue else 0f
    }

    private fun checkForAddParentheses(numberStr: String): String {
        return if (numberStr.toDouble() >= 0) {
            numberStr
        } else {
            "(${numberStr})"
        }
    }

    private fun checkForAddPlusSign(number: Float): String {
        return if (number >= 0) {
            "+${number.toString()}"
        } else {
            number.toString()
        }
    }

    private fun changeSign(number: Float): Float {
        return if (number == 0f || number == -0f) 0f else number * (-1)
    }

    private fun negativeZeroValidation(number: Float): Float = when (number) {
        -0f -> 0f
        else -> number
    }
}