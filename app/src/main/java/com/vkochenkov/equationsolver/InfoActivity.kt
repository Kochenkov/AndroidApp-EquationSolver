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
        var text = "Любое квадратное уравнение можно представить в общем виде: \\(ax^2+bx+c=0\\), "
        text += "где \\(a\\), \\(b\\) и \\(c\\) это числовые коэффициенты перед разными степенями x. "
        text += "Например, в уравнении \\(x^2+2x-4=0\\) коэффиициенты будут такие: \\(a=1\\), \\(b=2\\), \\(c=-4\\). "
        text += "Наше приложение умеет решать уравнения записанные именно в общем виде. Однако, уравнения в общем виде встречаются не так часто. "
        text += "Иногда, перед тем как решить уравнение, нужно сначала привести его к общему виду. "
        text += "Если в вашей задаче вы встретились с таким случаем, то не расстраивайтесь. "
        text += "Вот вам несколько примеров уравнений, которые приводятся к общему виду. "
        text += "Пример 1: \\(-3x^2=-5+0.5x\\) \\(\\Rightarrow\\) \\(-3x^2-0.5x+5=0\\). Пример 2: \\(11=x^2\\) \\(\\Rightarrow\\) \\(-x^2+11=0\\). "
        text += "Обратите внимание, что в качестве коэффициентов могут выступать не только целые, но и дробные числа, записанные в десятичном виде (пример 1). "
        text += "Также, обратите внимание на то, что какие-то из слогаемых уравнения общего вида могут отсутствовать (пример 2). "
        text += "При записи уравнения в нашем приложении, укажите перед таким коэффициентом 0."
        mvInfo.text = text
        //todo текст может не отображаться до конца и быть немного обрезан
    }
}