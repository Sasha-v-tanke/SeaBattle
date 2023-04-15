package com.direwolf.seabattle2.objects.placement

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.activities.PlacementActivity
import com.direwolf.seabattle2.R

class PlacementShip(
    private val context: Context, layout: ConstraintLayout, private val size: Int,
    private val x: Int, private val y: Int,
    private val length: Int, private val defaultPlace: Pair<Int, Int>)
{

    private var vertical = true
    private var textView = TextView(context)
    private var selected = false
    private var set = false

    init {
        textView.text = ""
        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.black_orange))
        textView.background = background
        textView.gravity = Gravity.CENTER

        val params = ConstraintLayout.LayoutParams(size, size * length)
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftMargin = x
        params.topMargin = y
        textView.layoutParams = params
        layout.addView(textView)

        textView.setOnClickListener {
            select()
        }
    }

    fun select() {
        if (selected) {
            rotate()
        } else {
            if (set) {
                (context as PlacementActivity).removeShip(this)
            }
            val newParams = ConstraintLayout.LayoutParams(size, size * length)
            newParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.leftMargin = defaultPlace.first
            newParams.topMargin = defaultPlace.second - length / 2 * size
            textView.layoutParams = newParams

            val background = GradientDrawable()
            background.setColor(ContextCompat.getColor(context, R.color.white))
            textView.background = background
            selected = true
            vertical = true
            set = false
            (context as PlacementActivity).shipSelect(this)
        }
    }

    fun rotate() {
        if (!selected) {
            return
        }
        //Log.w("rotate", "0")
        if (vertical) {
            vertical = false
            val newParams = ConstraintLayout.LayoutParams(size * length, size)
            newParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.leftMargin = defaultPlace.first - length / 2 * size
            newParams.topMargin = defaultPlace.second
            textView.layoutParams = newParams
        } else {
            vertical = true
            val newParams = ConstraintLayout.LayoutParams(size, size * length)
            newParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.leftMargin = defaultPlace.first
            newParams.topMargin = defaultPlace.second - length / 2 * size
            textView.layoutParams = newParams
        }
    }

    fun unselect() {
        if (selected) {
            val newParams = ConstraintLayout.LayoutParams(size, size * length)
            newParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            newParams.leftMargin = x
            newParams.topMargin = y
            textView.layoutParams = newParams

            val background = GradientDrawable()
            background.setColor(ContextCompat.getColor(context, R.color.black_orange))
            textView.background = background
            selected = false
            vertical = true
        }
    }

    fun reset(){
        (context as PlacementActivity).removeShip(this)
    }

    fun isSelected(): Boolean{
        return set
    }

    fun set(x: Int, y: Int) {
        set = true
        val newParams: ConstraintLayout.LayoutParams = if (vertical) {
            ConstraintLayout.LayoutParams(size, size * length)
        } else {
            ConstraintLayout.LayoutParams(size * length, size)
        }
        newParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        newParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        newParams.leftMargin = x
        newParams.topMargin = y
        textView.layoutParams = newParams

        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.orange_black))
        textView.background = background
        selected = false
        (context as PlacementActivity).shipSelect(this)
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