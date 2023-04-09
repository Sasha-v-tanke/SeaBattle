package com.direwolf.seabattle2.objects

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R

class Cell(context: Context, grid: GridLayout, x: Int, y: Int, size: Int){
    private var textView: TextView

    init {
        textView = TextView(context)
        textView.text = "$x $y"
        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.black_orange))
        background.setStroke(2, ContextCompat.getColor(context, R.color.orange_black))
        textView.background = background
        textView.setTextColor(ContextCompat.getColor(context, R.color.orange_black))

        textView.gravity = Gravity.CENTER

        textView.width = size
        textView.height = size

        val layoutParams = GridLayout.LayoutParams()
        layoutParams.rowSpec = GridLayout.spec(x, 1, 1f)
        layoutParams.columnSpec = GridLayout.spec(y, 1, 1f)
        grid.addView(textView, layoutParams)
        textView.setOnClickListener {
            onTouch()
        }
    }

    private fun onTouch(){

    }
}

class Cell2(context: Context, grid: GridLayout, x: Int, y: Int, size: Int){
    private var textView: TextView

    init {
        textView = TextView(context)
        textView.text = " "
        //textView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.black_orange))
        background.setStroke(0, ContextCompat.getColor(context, R.color.black_orange))
        textView.background = background
        textView.setTextColor(ContextCompat.getColor(context, R.color.black_orange))

        textView.gravity = Gravity.CENTER

        textView.width = size
        textView.height = size

        val layoutParams = GridLayout.LayoutParams()
        layoutParams.rowSpec = GridLayout.spec(x, 1, 1f)
        layoutParams.columnSpec = GridLayout.spec(y, 1, 1f)
        grid.addView(textView, layoutParams)
    }
}

class NoneCell(context: Context, grid: GridLayout, x: Int, y: Int, size: Int) {
    private var textView: TextView

    init {
        textView = TextView(context)
        textView.text = ""
        textView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.orange_black))
        background.setStroke(2, ContextCompat.getColor(context, R.color.orange_black))
        textView.background = background
        textView.setTextColor(ContextCompat.getColor(context, R.color.orange_black))

        textView.gravity = Gravity.CENTER

        textView.width = size
        textView.height = size

        val layoutParams = GridLayout.LayoutParams()
        layoutParams.rowSpec = GridLayout.spec(x, 1, 1f)
        layoutParams.columnSpec = GridLayout.spec(y, 1, 1f)
        grid.addView(textView, layoutParams)
    }
}