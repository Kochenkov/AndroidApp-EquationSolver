package com.vkochenkov.equationsolver.activities

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.vkochenkov.equationsolver.R
import com.vkochenkov.equationsolver.services.QuadraticEquation
import com.vkochenkov.equationsolver.views.DrawView
import io.github.kexanie.library.MathView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

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
    private lateinit var series: LineGraphSeries<DataPoint>
    private lateinit var animationRotateCenter: Animation
    private lateinit var scrollView: ScrollView
    private lateinit var tvCoordinates: TextView
    private lateinit var graphView: GraphView

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
        scrollView = findViewById(R.id.scrollMain)
        tvCoordinates = findViewById(R.id.tvCoordinates)
        graphView = findViewById(R.id.graphView)

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
        graphView.visibility = View.VISIBLE
        tvCoordinates.visibility = View.VISIBLE
        var xFrom: Double
        var xTo: Double
        if (equation.quadrX1.toDouble()<equation.quadrX2.toDouble()) {
            xFrom = equation.quadrX1.toDouble()
            xTo = equation.quadrX2.toDouble()
        } else {
            xFrom = equation.quadrX2.toDouble()
            xTo = equation.quadrX1.toDouble()
        }
        var y: Double
        var dif = abs(equation.quadrX1.toDouble() - equation.quadrX2.toDouble())/100
        series = LineGraphSeries()
        y = equation.a*(xFrom*xFrom) + equation.b*xFrom + equation.c
        series.appendData(DataPoint(xFrom,y), true, 101)
        for (i in 0..100) {
            if (xFrom+dif<xTo) {
                xFrom+=dif
            } else {
                break
            }
            y = equation.a*(xFrom*xFrom) + equation.b*xFrom + equation.c
            series.appendData(DataPoint(xFrom,y), true, 101)
        }
        graphView.addSeries(series)
        tvCoordinates.setText("Координаты вершины параболы: ${equation.quadrX0}; ${equation.quadrY0}")
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
        tvCoordinates.visibility = View.GONE
        graphView.visibility = View.GONE
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