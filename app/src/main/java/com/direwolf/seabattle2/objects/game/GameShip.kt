package com.direwolf.seabattle2.objects.game

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R

class GameShip (private val context: Context, layout: ConstraintLayout, private val size: Int,
                private val x: Int, private val y: Int,
                private val length: Int, private var vertical: Boolean)
{
    private var textView = TextView(context)
    private var set = false

    init {
        textView.text = ""
        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.white))
        textView.background = background
        textView.gravity = Gravity.CENTER

        val params: ConstraintLayout.LayoutParams
        if (vertical) {
            params = ConstraintLayout.LayoutParams(size, size * length)
        }
        else{
            params = ConstraintLayout.LayoutParams(size * length, size)
        }
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftMargin = x
        params.topMargin = y
        textView.layoutParams = params
        layout.addView(textView)

        textView.setOnClickListener {
            //select()
        }
    }

    fun getOrientation(): Boolean {
        return vertical
    }

    fun getLength(): Int {
        return length
    }

    fun getCoor(): Pair<Int, Int> {
        val params = textView.layoutParams as ConstraintLayout.LayoutParams
        val a = params.leftMargin
        val b = params.topMargin
        return Pair(a, b)
    }
}