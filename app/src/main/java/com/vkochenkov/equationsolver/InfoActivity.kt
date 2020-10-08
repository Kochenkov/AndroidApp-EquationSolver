package com.vkochenkov.equationsolver

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.github.kexanie.library.MathView
import androidx.appcompat.widget.Toolbar as Toolbar

class InfoActivity : AppCompatActivity() {

    lateinit var mvInfo: MathView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val toolbar: Toolbar = findViewById(R.id.toolbarSecond)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        mvInfo = findViewById(R.id.mvInfo)
        var text: String = "Любое квадратное уравнение можно представить в общем виде: \\(ax^2+bx+c\\)"
        //todo дописать текст
        mvInfo.text = text
    }
}