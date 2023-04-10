package com.direwolf.seabattle2.objects.game

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R
import com.direwolf.seabattle2.objects.placement.PlacementCell
import kotlin.reflect.KFunction2

class AIGrid(private val context: Context, private val layout: ConstraintLayout,
             private val size: Int, x: Int, y: Int,
             private val left: Int, private val top: Int,
             private val func: KFunction2<Int, Int, Unit>
)
{
    private var cells: Array<Array<PlacementCell>>
    private var cells2 = Array(x) { Array(y) { 0 } }
    private lateinit var ships: Array<GameShip>

    init {
        val bg = TextView(context)
        bg.text = ""
        val background = GradientDrawable()
        background.setColor(ContextCompat.getColor(context, R.color.white))
        bg.background = background
        bg.setTextColor(ContextCompat.getColor(context, R.color.black_orange))

        bg.gravity = Gravity.CENTER

        val params = ConstraintLayout.LayoutParams(size * x + 8, size * y + 8)
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftMargin = left - 4
        params.topMargin = top - 4
        bg.layoutParams = params
        layout.addView(bg)

        cells = Array(x) { i ->
            Array(y) { j ->
                createCell(left + i * size, top + j * size)
            }
        }
    }

    private fun createCell(x: Int, y: Int): PlacementCell {
        val cell = PlacementCell(context, layout, size, x, y)
        cell.getView().setOnClickListener {
            val a = (x - left) / size
            val b = (y - top) / size
            func(a, b)
        }
        return cell
    }

    fun boom(x:Int, y:Int): Boolean {
        return false
    }

    fun check(): Boolean {
        return false
    }

    fun setShips(coors:  Array<Int>) {
        ships = emptyArray()
        val count = coors.size / 4
        for (i in 0 until count) {
            val x = (coors[i * 4]) * size + left
            val y = (coors[i * 4 + 1]) * size + top
            val len = coors[i * 4 + 2] + 1
            val ver = coors[i * 4 + 3]
            Log.w("ai", "$x $y $len $ver")
            val ship = GameShip(context, layout, size, x, y, len, ver == 1)
            ships += ship
        }
    }
}