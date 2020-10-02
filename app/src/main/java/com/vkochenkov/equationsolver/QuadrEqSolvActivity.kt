package com.vkochenkov.equationsolver

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class QuadrEqSolvActivity : AppCompatActivity(), View.OnClickListener {

    val edtArr: Array<Int> = arrayOf(R.id.edtA, R.id.edtB, R.id.edtC)
    val btnArr: Array<Int> = arrayOf(R.id.btnChangeSignA, R.id.btnChangeSignB, R.id.btnChangeSignC, R.id.btnClear, R.id.btnSolve, R.id.btnInfo)

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quadr_eq_solv)

        //set button's click listener
        for (i in btnArr) {
            findViewById<View>(i).setOnClickListener(this)
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnInfo -> { }//todo
            R.id.btnChangeSignA -> changeSign(view.id)
            R.id.btnChangeSignB -> changeSign(view.id)
            R.id.btnChangeSignC -> changeSign(view.id)
            R.id.btnClear -> clearFields()
            R.id.btnSolve -> { }//todo
        }
    }

    private fun changeSign(viewId: Int) {
        val sign: String = findViewById<Button>(viewId).text.toString()
        findViewById<Button>(viewId).text = when (sign) {
            "+" -> "-"
            "-" -> if (viewId==R.id.btnChangeSignA) "" else "+"
            else -> "-"
        }
    }

    private fun clearFields() {
        for (i in edtArr) {
            findViewById<EditText>(i).setText("")
        }
        findViewById<Button>(R.id.btnChangeSignA).text = ""
        findViewById<Button>(R.id.btnChangeSignB).text = "+"
        findViewById<Button>(R.id.btnChangeSignC).text = "+"
    }
}