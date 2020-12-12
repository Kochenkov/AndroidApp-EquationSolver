package com.vkochenkov.equationsolver.activities

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.vkochenkov.equationsolver.R
import com.vkochenkov.equationsolver.services.QuadraticEquation
import com.vkochenkov.equationsolver.views.DrawView
import io.github.kexanie.library.MathView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var localisationStrings: HashMap<String, String> = HashMap()

    private lateinit var equation: QuadraticEquation

    private lateinit var edtA: EditText
    private lateinit var edtB: EditText
    private lateinit var edtC: EditText
    private lateinit var btnChangeSignA: Button
    private lateinit var btnChangeSignB: Button
    private lateinit var btnChangeSignC: Button
    private lateinit var btnClear: Button
    private lateinit var btnSolve: Button
    private lateinit var btnDraw: Button
    private lateinit var mvSolution: MathView
    private lateinit var drawView: DrawView
    private lateinit var animationRotateCenter: Animation
    private lateinit var scrollView: ScrollView

    private var phoneScreenWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbarMain))
        findDisplaySize()

        localisationStrings.put("answer", getString(R.string.answer))
        localisationStrings.put(
            "wrongAnswer",
            getString(R.string.wrong_answer)
        )
        localisationStrings.put(
            "solution",
            getString(R.string.solution)
        )
        localisationStrings.put(
            "solutionDiscrim",
            getString(R.string.solution_discrim)
        )
        localisationStrings.put("yourEq", getString(R.string.your_eq))
        localisationStrings.put(
            "noNaturalSolution",
            getString(R.string.no_natural_solution)
        )

        edtA = findViewById(R.id.edtA)
        edtB = findViewById(R.id.edtB)
        edtC = findViewById(R.id.edtC)
        btnChangeSignA = findViewById(R.id.btnChangeSignA)
        btnChangeSignB = findViewById(R.id.btnChangeSignB)
        btnChangeSignC = findViewById(R.id.btnChangeSignC)
        btnClear = findViewById(R.id.btnClear)
        btnSolve = findViewById(R.id.btnSolve)
        btnDraw = findViewById(R.id.btnDraw)
        mvSolution = findViewById(R.id.mvSolution)
        // drawView = findViewById(R.id.drawView)
        scrollView = findViewById(R.id.scrollMain)

        btnChangeSignA.setOnClickListener(this)
        btnChangeSignB.setOnClickListener(this)
        btnChangeSignC.setOnClickListener(this)
        btnClear.setOnClickListener(this)
        btnSolve.setOnClickListener(this)
        btnDraw.setOnClickListener(this)

        animationRotateCenter = AnimationUtils.loadAnimation(
            this, R.anim.rotate_center
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info -> openInfoActivity()
            R.id.action_share -> share()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun share() {
        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        val text = getString(R.string.share_link)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, "Send message..."))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnChangeSignA -> changeSign(btnChangeSignA)
            R.id.btnChangeSignB -> changeSign(btnChangeSignB)
            R.id.btnChangeSignC -> changeSign(btnChangeSignC)
            R.id.btnClear -> clearFields()
            R.id.btnSolve -> solveEq()
            R.id.btnDraw -> drawDiagram()
        }
    }

    private fun drawDiagram() {

        //todo
        val pairX1Y1 = Pair(equation.quadrX1, equation.quadrY)
        val pairX2Y2 = Pair(equation.quadrX2, equation.quadrY)
        val pairX0Y0 = Pair(equation.quadrX0, equation.quadrY0)
        val pointsArr = arrayListOf<Float>(
            pairX1Y1.first, pairX1Y1.second,
            pairX2Y2.first, pairX2Y2.second,
            pairX0Y0.first, pairX0Y0.second
        )
//        drawView.drawDiagram(pointsArr)
////        drawView.visibility = View.VISIBLE

        val drawViewSize = (phoneScreenWidth / 12) * 10
        drawView = DrawView(this, drawViewSize, pointsArr)
        val params: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(drawViewSize, drawViewSize)
        params.gravity = Gravity.CENTER_HORIZONTAL
        params.setMargins(
            drawViewSize / 10,
            drawViewSize / 10,
            drawViewSize / 10,
            drawViewSize / 10
        )
        drawView.layoutParams = params
        val mathLayout = findViewById<LinearLayout>(R.id.layout_math)
        mathLayout.addView(drawView)
        drawView.invalidate()

        //скролл вниз, что бы был виден график
        //todo похоже что не робит как надо
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("eqValue", mvSolution.text?.toString())
        outState.putString("aSignValue", btnChangeSignA.text.toString())
        outState.putString("bSignValue", btnChangeSignB.text.toString())
        outState.putString("cSignValue", btnChangeSignC.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mvSolution.text = savedInstanceState.getString("eqValue")
        btnChangeSignA.text = savedInstanceState.getString("aSignValue")
        btnChangeSignB.text = savedInstanceState.getString("bSignValue")
        btnChangeSignC.text = savedInstanceState.getString("cSignValue")
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
        btnDraw.visibility = View.GONE
        //todo краш?
        drawView.visibility = View.GONE
    }

    private fun solveEq() {
        try {
            //убираем клаву, если открыта
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } finally {
            //передаем значения в объект уравнения и показываем ответ
            val aPair = Pair(
                btnChangeSignA.text.toString(),
                if (edtA.text.toString() == "") "1" else if (edtA.text.toString() == ".") "0.1" else edtA.text.toString()
            )
            val bPair = Pair(
                btnChangeSignB.text.toString(),
                if (edtB.text.toString() == "") "1" else if (edtB.text.toString() == ".") "0.1" else edtB.text.toString()
            )
            val cPair = Pair(
                btnChangeSignC.text.toString(),
                if (edtC.text.toString() == "") "0" else if (edtC.text.toString() == ".") "0.1" else edtC.text.toString()
            )

            equation = QuadraticEquation(aPair, bPair, cPair, localisationStrings)
            mvSolution.text = equation.toString()
            //todo не работает с первого раза. Нужно придумать, как делать скролл вниз после нажатия на кнопку / возможно это сзано с фокусом в эдит тексте после нажатия на кнопку
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)

            //делаем видимой кнопку которая рисует график
            //todo нужно засунуть в какой-нибудь асинк-таск, тк кнопка появляется быстрее, чем mathView
            btnDraw.visibility = View.VISIBLE
        }
    }

    private fun findDisplaySize() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        phoneScreenWidth = metrics.widthPixels
    }
}