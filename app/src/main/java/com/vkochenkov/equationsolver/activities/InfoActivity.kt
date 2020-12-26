package com.vkochenkov.equationsolver.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.vkochenkov.equationsolver.R
import io.github.kexanie.library.MathView

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val toolbar: Toolbar = findViewById(R.id.toolbarInfo)
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val mvInfo: MathView = findViewById(R.id.mvInfo)
        mvInfo.text = getString(R.string.long_info_text)
    }
}