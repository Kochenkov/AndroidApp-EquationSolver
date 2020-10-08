package com.vkochenkov.equationsolver

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.IDNA
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.kexanie.library.MathView
import kotlinx.android.synthetic.main.activity_main.*

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
            R.id.btnInfo -> openInfoActivity()
            R.id.btnChangeSignA -> changeSign(btnChangeSignA)
            R.id.btnChangeSignB -> changeSign(btnChangeSignB)
            R.id.btnChangeSignC -> changeSign(btnChangeSignC)
            R.id.btnClear -> clearFields()
            R.id.btnSolve -> solveEq()
        }
    }

    private fun openInfoActivity() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
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
        try {
            //убираем клаву, если открыта
            val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } finally {
            //передаем значения в объект уравнения и показываем ответ
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
//todo не работает. Нужно придумать, как делать скролл вниз после нажатия на кнопку / возможно это сзано с фокусом в эдит тексте после нажатия на кнопку
            val scrollView = findViewById<ScrollView>(R.id.scrollMain)
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}