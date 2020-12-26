
        edtA = findViewById(R.id.edtA)
        edtB = findViewById(R.id.edtB)
        edtC = findViewById(R.id.edtC)
        btnChangeSignA = findViewById(R.id.btnChangeSignA)
        btnChangeSignB = findViewById(R.id.btnChangeSignB)
        btnChangeSignC = findViewById(R.id.btnChangeSignC)
        btnClear = findViewById(R.id.btnClear)
        btnSolve = findViewById(R.id.btnSolve)
        mvSolution = findViewById(R.id.mvSolution)

        btnChangeSignA.setOnClickListener(this)
        btnChangeSignB.setOnClickListener(this)
        btnChangeSignC.setOnClickListener(this)
        btnClear.setOnClickListener(this)
        btnSolve.setOnClickListener(this)

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
        }
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

            val equation: QuadraticEquation = QuadraticEquation(
                aPair,
                bPair,
                cPair,
                localisationStrings
            )
            mvSolution.text = equation.toString()
            //todo не работает с первого раза. Нужно придумать, как делать скролл вниз после нажатия на кнопку / возможно это сзано с фокусом в эдит тексте после нажатия на кнопку
            val scrollView = findViewById<ScrollView>(R.id.scrollMain)
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}