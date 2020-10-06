package com.vkochenkov.equationsolver

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import io.github.kexanie.library.MathView


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var edtA: EditText
    lateinit var edtB: EditText
    lateinit var edtC: EditText
    lateinit var btnChangeSignA: Button
    lateinit var btnChangeSignB: Button
    lateinit var btnChangeSignC: Button
    lateinit var btnClear: Button
    lateinit var btnSolve: Button
    lateinit var btnInfo: ImageButton
    lateinit var mvSolution: MathView

    lateinit var animationRotateCenter: Animation

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtA = findViewById(R.id.edtA)
        edtB = findViewById(R.id.edtB)
        edtC = findViewById(R.id.edtC)
        btnChangeSignA = findViewById(R.id.btnChangeSignA)
        btnChangeSignB = findViewById(R.id.btnChangeSignB)
        btnChangeSignC = findViewById(R.id.btnChangeSignC)
        btnClear = findViewById(R.id.btnClear)
        btnSolve = findViewById(R.id.btnSolve)
        btnInfo = findViewById(R.id.btnInfo)
        mvSolution = findViewById(R.id.mvSolution)
        //set button's click listener
        btnChangeSignA.setOnClickListener(this)
        btnChangeSignB.setOnClickListener(this)
        btnChangeSignC.setOnClickListener(this)
        btnClear.setOnClickListener(this)
        btnSolve.setOnClickListener(this)
        btnInfo.setOnClickListener(this)

        animationRotateCenter = AnimationUtils.loadAnimation(
            this, R.anim.rotate_center
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnInfo -> {
            }//todo
            R.id.btnChangeSignA -> changeSign(btnChangeSignA)
            R.id.btnChangeSignB -> changeSign(btnChangeSignB)
            R.id.btnChangeSignC -> changeSign(btnChangeSignC)
            R.id.btnClear -> clearFields()
            R.id.btnSolve -> solveEq()
        }
    }

    private fun changeSign(button: Button) {
        button.startAnimation(animationRotateCenter)
        val sign: String = button.text.toString()
        button.text = when (sign) {
            "+" -> "-"
            "-" -> if (button == btnChangeSignA) "" else "+"
            else -> "-"
        }
    }

    private fun clearFields() {
        btnChangeSignA.startAnimation(animationRotateCenter)
        btnChangeSignB.startAnimation(animationRotateCenter)
        btnChangeSignC.startAnimation(animationRotateCenter)
        btnChangeSignA.text = ""
        btnChangeSignB.text = "+"
        btnChangeSignC.text = "+"
        edtA.setText("")
        edtB.setText("")
        edtC.setText("")
        mvSolution.text = ""
    }

    private fun solveEq() {
        val aPair = Pair(
            btnChangeSignA.text.toString(),
            if (edtA.text.toString() == "") "1" else edtA.text.toString()
        )
        val bPair = Pair(
            btnChangeSignB.text.toString(),
            if (edtB.text.toString() == "") "1" else edtB.text.toString()
        )
        val cPair = Pair(
            btnChangeSignC.text.toString(),
            if (edtC.text.toString() == "") "0" else edtC.text.toString()
        )

        val equation: QuadraticEquation = QuadraticEquation(aPair, bPair, cPair)
        mvSolution.text = equation.toString()
        //todo wooden leg (do not work correctly)
        mvSolution.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}