package com.direwolf.seabattle2.objects.game

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R
import kotlin.reflect.KFunction2

class GameShipAI (private val context: Context, layout: ConstraintLayout, private val size: Int,
                  private val x: Int, private val y: Int,
                  private val length: Int, private var vertical: Boolean,
                  private val func: KFunction2<Int, Int, Unit>)
{
    private var textViews = emptyArray<TextView>()
    private var cells = Array(length) { 0 }
    private var set = false
    private var destroyed = false

    init {
        for (i in 0 until length) {
            val textView = TextView(context)
            textView.text = ""
            val background = GradientDrawable()
            background.setColor(ContextCompat.getColor(context, R.color.p_c_bg_1))
            background.setStroke(4, ContextCompat.getColor(context, R.color.p_c_br_1))
            textView.background = background
            textView.gravity = Gravity.CENTER
            val params = ConstraintLayout.LayoutParams(size, size)
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            if (vertical) {
                params.leftMargin = x
                params.topMargin = y + size * i
            } else {
                params.leftMargin = x + size * i
                params.topMargin = y
            }
            textView.layoutParams = params
            //textView.visibility = View.INVISIBLE
            textView.setOnClickListener {
                var a = x
                var b = y
                if (vertical) {
                    b += i * size
                } else {
                    a += i * size
                }
                if (cells[i] == 0) {
                    cells[i] = 1
                    if (0 !in cells) {
                        //Log.w("ship self", "00001")
                        destroyed = true
                    }
                    func(a, b)
                }
            }
            textViews += textView
            layout.addView(textView)
        }
    }

    fun getCells(): Array<Pair<Int, Int>> {
        var a = emptyArray<Pair<Int, Int>>()
        for (i in 0 until length) {
            a += if (vertical) {
                Pair(x, y + i * size)
            } else {
                Pair(x + i * size, y)
            }
        }
        return a
    }

    fun show(){
        for (i in 0 until length){
            if (cells[i] != 1){
                val background = GradientDrawable()
                background.setColor(ContextCompat.getColor(context, R.color.green))
                background.setStroke(4, ContextCompat.getColor(context, R.color.white))
                textViews[i].background = background
            }
        }
    }

    fun isDestroyed(): Boolean {
        //Log.w("check ship", destroyed.toString())
        return destroyed
    }
}

class GameShipPlayer (context: Context, layout: ConstraintLayout, private val size: Int,
                  private val x: Int, private val y: Int,
                  private val length: Int, private var vertical: Boolean)
{
    private var textViews = emptyArray<TextView>()
    private var cells = Array(length) { 0 }
    private var set = false
    private var destroyed = false

    init {
        for (i in 0 until length) {
            val textView = TextView(context)
            textView.text = ""
            val background = GradientDrawable()
            background.setColor(ContextCompat.getColor(context, R.color.white))
            textView.background = background
            textView.gravity = Gravity.CENTER
            textView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            val params = ConstraintLayout.LayoutParams(size, size)
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            if (vertical) {
                params.leftMargin = x
                params.topMargin = y + size * i
            } else {
                params.leftMargin = x + size * i
                params.topMargin = y
            }
            textView.layoutParams = params
            layout.addView(textView)
            textViews += textView
        }
    }

    fun boom(a: Int, b: Int) {
        if (vertical) {
            cells[(b - y) / size] = 1
        } else {
            cells[(a - x) / size] = 1
        }
        if (0 !in cells) {
            destroyed = true
        }
    }

    fun isDestroyed(): Boolean {
        return destroyed
    }

    fun getCells(): Array<Pair<Int, Int>> {
        var a = emptyArray<Pair<Int, Int>>()
        for (i in 0 until length) {
            a += if (vertical) {
                Pair(x, y + i * size)
            } else {
                Pair(x + i * size, y)
            }
        }
        return a
    }

    fun getOrientation(): Boolean {
        return vertical
    }

    fun getLength(): Int {
        return length
    }
}