package com.vkochenkov.equationsolver.activities

import android.content.Intent
import android.os.Bundle
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
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.vkochenkov.equationsolver.R
import com.vkochenkov.equationsolver.services.GraphicType
import com.vkochenkov.equationsolver.services.QuadraticEquation
import io.github.kexanie.library.MathView
import kotlin.math.abs

class MainActivity : AppCompatActivity(), View.OnClickListener {

    inner class ScrollThread : Thread() {
        override fun run() {
            sleep(500)
            runOnUiThread(Runnable {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            })
        }
    }

    inner class ShowDrawButtonThread : Thread() {
        override fun run() {
            sleep(300)
            runOnUiThread(Runnable {
                if (equation.graphicType != GraphicType.NO_GRAPHIC) {
                    btnDraw.visibility = View.VISIBLE
                }
            })
        }
    }

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
    private lateinit var series: LineGraphSeries<DataPoint>
    private lateinit var animationRotateCenter: Animation
    private lateinit var scrollView: ScrollView
    private lateinit var tvCoordinates: MathView
    private lateinit var graphView: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbarMain))

        //строковые данные, используемые в классе уравнения
        localisationStrings.put("answer", getString(R.string.answer))
        localisationStrings.put("wrongAnswer", getString(R.string.wrong_answer))
        localisationStrings.put("solution", getString(R.string.solution))
        localisationStrings.put("solutionDiscrim", getString(R.string.solution_discrim))
        localisationStrings.put("yourEq", getString(R.string.your_eq))
        localisationStrings.put("noNaturalSolution", getString(R.string.no_natural_solution))

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
        scrollView = findViewById(R.id.scrollMain)
        tvCoordinates = findViewById(R.id.mvCoordinates)
        graphView = findViewById(R.id.graphView)

        btnChangeSignA.setOnClickListener(this)
        btnChangeSignB.setOnClickListener(this)
        btnChangeSignC.setOnClickListener(this)
        btnClear.setOnClickListener(this)
        btnSolve.setOnClickListener(this)
        btnDraw.setOnClickListener(this)

        animationRotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate_center)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info -> openInfoActivity()
            R.id.action_share -> share()
            R.id.action_exit -> exit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exit() {
        onBackPressed()
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
        btnDraw.visibility = View.GONE
        graphView.removeAllSeries()
        graphView.visibility = View.VISIBLE
        tvCoordinates.visibility = View.VISIBLE
        var text = ""
        series = LineGraphSeries()
        if (equation.graphicType == GraphicType.QUADRATIC) {
            var xFrom = equation.quadrX1.toDouble()
            var xTo = equation.quadrX2.toDouble()
            var dif = abs(equation.quadrX1.toDouble() - equation.quadrX2.toDouble()) / 100
            if (equation.quadrX1.toDouble() > equation.quadrX2.toDouble()) {
                xFrom = equation.quadrX2.toDouble()
                xTo = equation.quadrX1.toDouble()
            }
            if ((equation.quadrX1.toDouble() == equation.quadrX2.toDouble())) {
                xFrom = -5.0
                xTo = 5.0
                dif = 0.1
            }
            var y: Double
            //первая точка
            y = equation.a * (xFrom * xFrom) + equation.b * xFrom + equation.c
            series.appendData(DataPoint(xFrom, y), true, 102)
            //считаем в цикле остальные точки
            for (i in 0..100) {
                if (xFrom + dif < xTo) {
                    xFrom += dif
                } else {
                    break
                }
                y = equation.a * (xFrom * xFrom) + equation.b * xFrom + equation.c
                series.appendData(DataPoint(xFrom, y), true, 102)
            }
            text += getString(R.string.coordinates_parabola_text)
            text += "$$\\ x_0 = {${equation.quadrX0}}; y_0 = {${equation.quadrY0}} $$"
        } else {
            var x = equation.linearX.toDouble()
            var y = equation.linearY.toDouble()
            series.appendData(DataPoint(x, y), true, 12)
            for (i in 0..10) {
                x += 1
                y = equation.b * x + equation.c
                series.appendData(DataPoint(x, y), true, 12)
            }
        }
        text += getString(R.string.graph_title_text)
        tvCoordinates.setText(text)
        //todo нужно разобраться как скейлить график, пока это плохо работает
//        graphView.getViewport().setScalable(true);
//        graphView.getViewport().setScalableY(true);
        graphView.addSeries(series)
        val scrollThread = ScrollThread()
        scrollThread.start()
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
        tvCoordinates.visibility = View.GONE
        graphView.visibility = View.GONE
    }

    private fun solveEq() {
        //убираем график и кнопку, если есть
        tvCoordinates.visibility = View.GONE
        graphView.visibility = View.GONE
        btnDraw.visibility = View.GONE
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
            mvSolution.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            mvSolution.visibility = View.VISIBLE
            val showDrawButton = ShowDrawButtonThread()
            showDrawButton.start()
            val scrollDown = ScrollThread()
            scrollDown.start()
        }
    }
}