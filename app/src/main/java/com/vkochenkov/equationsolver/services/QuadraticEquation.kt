package com.vkochenkov.equationsolver.services

import kotlin.math.sqrt

const val DISCRIMINANT_FORMULA: String = "$$\\ D = b^2-4ac $$"
const val QUADRATIC_X12_FORMULA: String = "$$\\ x_{1,2} = {-b \\pm \\sqrt{D} \\over 2a}$$"
const val QUADRATIC_X_FORMULA: String = "$$\\ x = {-b \\over 2a}$$"
const val LINEAR_X_FORMULA: String = "$$\\ x = {-c \\over b}$$"
const val EQUATION_PATTERN : String = "$$\\ ax^2+bx+c $$"

enum class GraphicType {
    QUADRATIC, LINEAR, NO_GRAPHIC
}

class QuadraticEquation (
    val aPair: Pair<String, String>,
    val bPair: Pair<String, String>,
    val cPair: Pair<String, String>,
    val localisationStrings: HashMap<String, String>
) {
    //тип графика. По-дефолту - квадратное
    var graphicType: GraphicType = GraphicType.QUADRATIC

    //коэффициенты уравнения
    val a = glueSignWithNumber(aPair)
    val b = glueSignWithNumber(bPair)
    val c = glueSignWithNumber(cPair)

    //дискриминант
    private val d: Float = (this.b * this.b) - (4 * this.a * this.c)

    //точки квадратного уравнения
    val quadrX1 = negativeZeroValidation((changeSign(b) + (sqrt(d))) / (2 * a))
    val quadrX2 = negativeZeroValidation((changeSign(b) - (sqrt(d))) / (2 * a))
    val quadrX0 = negativeZeroValidation(-b/(2*a))
    val quadrY0 = negativeZeroValidation(a*quadrX0*quadrX0 + b*quadrX0 + c)

    //точки линейного уравнения
    val linearX = negativeZeroValidation((changeSign(c))/b)
    val linearY = negativeZeroValidation(b*linearX+c)

    override fun toString(): String {
        var str = showBasicEquation()
        str += showCoefficients()
        str += if (a!=0f) showSolutionForQuadraticEquation() else if (b!=0f) showSolutionForLinearEquation() else showError()
        return str
    }

    private fun showBasicEquation(): String {
        var str = localisationStrings.get("yourEq").toString()
        var eq = when (a) {
            0f -> ""
            1f -> "x^2"
            -1f -> "-x^2"
            else -> "${a}x^2"
        }
        eq += when (b) {
            0f -> ""
            1f -> {if (eq!="") "+x" else "x"}
            -1f -> {if (eq!="") "-x" else "x"}
            else -> {if (eq!="") "${checkForAddPlusSign(b)}x" else "${b}x"}
        }
        eq += when (c) {
            0f -> ""
            else -> {if (eq!="") checkForAddPlusSign(c) else c}
        }
        if (eq=="") {
            eq += "0"
        }
        eq += "=0"
        str += "$$\\ $eq $$"
        return str
    }

    private fun showCoefficients(): String {
        return "$$\\ a=${a}; b=${b}; c=${c} $$"
    }

    private fun showSolutionForQuadraticEquation(): String {
        graphicType = GraphicType.QUADRATIC
        var str = localisationStrings.get("solutionDiscrim").toString()
        str += DISCRIMINANT_FORMULA
        str += "$$\\ D = ${checkForAddParentheses(b)}^2 - 4 * ${checkForAddParentheses(a)} * ${checkForAddParentheses(c)} $$ "
        str += "$$\\ D = ${d} $$"
        if (d > 0) {
            str += QUADRATIC_X12_FORMULA
            str += "$$\\ x_1 = {${changeSign(b)} + \\sqrt{${d}} \\over 2*${checkForAddParentheses(a)}}$$"
            str += "$$\\ x_2 = {${changeSign(b)} - \\sqrt{${d}} \\over 2*${checkForAddParentheses(a)}}$$"
            str += localisationStrings.get("answer").toString()
            str += "$$\\ x_1 = ${quadrX1}; x_2 = ${quadrX2}$$"
        } else if (d==0f) {
            str += QUADRATIC_X_FORMULA
            str += "$$\\ x = {${changeSign(b)} \\over 2*${checkForAddParentheses(a)}}$$"
            str += localisationStrings.get("answer").toString()
            str += "$$\\ x = ${quadrX1}$$"
        } else {
            graphicType = GraphicType.NO_GRAPHIC
            str += localisationStrings.get("answer").toString() + " "
            str += localisationStrings.get("noNaturalSolution").toString()
        }
        return str
    }

    private fun showSolutionForLinearEquation(): String {
        graphicType = GraphicType.LINEAR
        var str = localisationStrings.get("solution").toString()
        str += LINEAR_X_FORMULA
        str += "$$\\ x = {${changeSign(c)} \\over ${b}}$$"
        str += localisationStrings.get("answer").toString()
        str += "$$\\ x = ${linearX} $$"
        return str
    }

    private fun showError(): String {
        graphicType = GraphicType.NO_GRAPHIC
        return localisationStrings.get("wrongAnswer").toString()
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

    private fun checkForAddPlusSign(number: Float): String {
        return if (number>=0) {
            "+${number.toString()}"
        } else {
            number.toString()
        }
    }

    private fun changeSign(number: Float): Float {
        return if (number==0f || number==-0f) 0f else number*(-1)
    }

    private fun negativeZeroValidation(number: Float): Float = when (number) {
        -0f -> 0f
        else -> number
    }
}