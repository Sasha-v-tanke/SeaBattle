package com.direwolf.seabattle2.objects

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R

class Ship1(context: Context, grid: GridLayout, length: Int, x: Int, y: Int, size: Int):
    Ship(context, grid, length, x, y, size, false) {
        init{
            bgColor = ContextCompat.getColor(context, R.color.black_orange)
            createView(context, grid, length, x, y, size, false)

        }
}

open class Ship(context: Context, grid: GridLayout,
                private val length: Int, x: Int, y: Int, size: Int, private val vertical: Boolean)
{
    protected lateinit var textView: TextView
    protected var bgColor: Int

    init {
        bgColor = ContextCompat.getColor(context, R.color.orange_black)
        createView(context, grid, length, x, y, size, vertical)
    }
    protected fun createView(context: Context, grid: GridLayout,
                           length: Int, x: Int, y: Int, size: Int, vertical: Boolean){

        textView = TextView(context)
        textView.text = ""
        textView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        val background = GradientDrawable()
        background.setColor(bgColor)
        textView.background = background
        textView.gravity = Gravity.CENTER
        val layoutParams: GridLayout.LayoutParams
        if (!vertical) {
            textView.width = size
            textView.height = size
            layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(x, 1, 1f),
                GridLayout.spec(y, length, 1f)
            )
        }
        else{
            textView.width = size
            textView.height = size
            layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(x, length, 1f),
                GridLayout.spec(y, 1, 1f)
            )
        }
        grid.addView(textView, layoutParams)
    }

    fun getOrientation(): Boolean{
        return vertical
    }

    fun getLength(): Int {
        return length
    }

    fun getView(): TextView{
        return textView
    }
}

class SelectedShip(context: Context, grid: GridLayout, length: Int, x: Int, y: Int,
                   size: Int, vertical: Boolean):
    Ship(context, grid, length, x, y, size, vertical)
{
    init {
        bgColor = ContextCompat.getColor(context, R.color.white)
        createView(context, grid, length, x, y, size, vertical)
    }
}
