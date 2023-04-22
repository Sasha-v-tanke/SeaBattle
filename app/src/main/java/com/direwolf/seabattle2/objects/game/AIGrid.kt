package com.direwolf.seabattle2.objects.game

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R
import com.direwolf.seabattle2.objects.placement.PlacementCell
import java.util.Collections.min
import kotlin.math.min
import kotlin.reflect.KFunction2

class AIGrid(private val context: Context, private val layout: ConstraintLayout,
             private val size: Int, x: Int, y: Int,
             private val left: Int, private val top: Int,
             private val func: KFunction2<Int, Int, Unit>
) {
    private var cells: Array<Array<PlacementCell>>
    private var cells2 = Array(x) { Array(y) { 0 } }
    private lateinit var ships: Array<GameShipAI>

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
            if (cells2[a][b] != -1) {
                func(a, b)
            }
        }
        return cell
    }

    fun boom(x: Int, y: Int): Pair<Boolean, Boolean> {
        val flag = cells2[x][y] > 0
        var flag2 = false
        //Log.w("player", "$x $y")
        if (flag) {
            for (ship in ships) {
                if (Pair(x * size + left, y * size + top) in ship.getCells()) {
                    flag2 = ship.isDestroyed()
                    if (flag2) {
                        //Toast.makeText(context, "Destroy", Toast.LENGTH_SHORT).show()
                        cells2[x][y] = -1
                        createBigBoom(ship)
                    }
                    break
                }
            }
        }
        cells2[x][y] = -1
        createBoom(flag, x, y)

        return Pair(flag, flag2)
    }

    fun showShips(){
        for (ship in ships){
            if (!ship.isDestroyed()){
                ship.show()
            }
        }
    }

    private fun createBigBoom(ship: GameShipAI) {
        val p = ship.getCells()
        var a = emptyArray<Int>()
        var b = emptyArray<Int>()
        for (e in p) {
            a += e.first
            b += e.second
        }
        val right = (a.max() - left) / size + 1
        val l = (a.min() - left) / size - 1
        val bottom = (b.max() - top) / size + 1
        val t = (b.min() - top) / size - 1
        for (x in l..right) {
            for (y in t..bottom) {
                if (Pair(x * size + left, y * size + top) !in p) {
                    createBoom(false, x, y)
                }
            }
        }
    }

    private fun createBoom(isShip: Boolean, x: Int, y: Int) {
        if (x !in 0..9 || y !in 0..9) {
            return
        }
        val boomView = TextView(context)
        boomView.text = ""
        val background = GradientDrawable()

        if (isShip) {
            //boomView.text = "X"
            //boomView.setTextColor(ContextCompat.getColor(context, R.color.red))
            background.setColor(ContextCompat.getColor(context, R.color.red))
        } else {
            //boomView.text = "O"
            cells2[x][y] = -1
            boomView.setTextColor(ContextCompat.getColor(context, R.color.white))
            background.setColor(ContextCompat.getColor(context, R.color.light_blue_900))
        }
        boomView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        boomView.background = background
        boomView.gravity = Gravity.CENTER

        val params = ConstraintLayout.LayoutParams(size, size)
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftMargin = left + x * size
        params.topMargin = top + y * size
        boomView.layoutParams = params
        layout.addView(boomView)
    }

    fun setShips(coors: Array<Int>) {
        ships = emptyArray()
        val count = coors.size / 4
        for (i in 0 until count) {
            val x = (coors[i * 4]) * size + left
            val y = (coors[i * 4 + 1]) * size + top
            val len = coors[i * 4 + 2] + 1
            val ver = coors[i * 4 + 3]
            //Log.w("ai", "$x $y $len $ver")
            val ship = GameShipAI(context, layout, size, x, y, len, ver == 1, ::f)
            for (j in 0 until coors[i * 4 + 2] + 1) {
                if (ver == 1) {
                    cells2[coors[4 * i]][coors[4 * i + 1] + j] = 1
                } else {
                    cells2[coors[4 * i] + j][coors[4 * i + 1]] = 1
                }
            }
            ships += ship
        }
    }

    private fun f(x: Int, y: Int) {
        val a = (x - left) / size
        val b = (y - top) / size
        func(a, b)
    }
}