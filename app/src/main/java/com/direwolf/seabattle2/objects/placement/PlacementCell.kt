package com.direwolf.seabattle2.objects.placement

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R

class PlacementCell(context: Context, layout: ConstraintLayout, size: Int,
                    private val x: Int, private val y: Int)
{
    private var textView: TextView = TextView(context)

    init {
        textView.text = ""
        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.light_blue_600))
        background.setStroke(4, ContextCompat.getColor(context, R.color.dark_blue))
        textView.background = background
        textView.setTextColor(ContextCompat.getColor(context, R.color.white))

        textView.gravity = Gravity.CENTER

        val params = ConstraintLayout.LayoutParams(size, size)
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftMargin = x
        params.topMargin = y
        textView.layoutParams = params
        layout.addView(textView)
    }
    fun getView(): TextView {
        return textView
    }
    fun getCoor(): Pair<Int, Int>{
        return Pair(x, y)
    }
}