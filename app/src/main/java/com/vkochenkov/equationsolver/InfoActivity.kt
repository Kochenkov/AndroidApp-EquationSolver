package com.vkochenkov.equationsolver

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.github.kexanie.library.MathView
import androidx.appcompat.widget.Toolbar as Toolbar

class InfoActivity : AppCompatActivity() {

    lateinit var mvInfo: MathView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("лайфцайкл","он create")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        toolbar = findViewById(R.id.toolbarInfo)
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        mvInfo = findViewById(R.id.mvInfo)
        mvInfo.text = getString(R.string.long_info_text)
        //todo текст может не отображаться до конца и быть немного обрезан
    }
}