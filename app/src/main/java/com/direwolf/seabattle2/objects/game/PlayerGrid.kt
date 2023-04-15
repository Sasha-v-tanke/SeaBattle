package com.direwolf.seabattle2.objects.game

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.direwolf.seabattle2.R

class PlayerGrid(private val context: Context, private val layout: ConstraintLayout,
                 private val size: Int, x: Int, y: Int,
                 private val left: Int, private val top: Int) {
    private var cells: Array<Array<GameCell>>
    private var cells2 = Array(x) { Array(y) { 0 } }
    private lateinit var ships: Array<GameShipPlayer>

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

    private fun createCell(x: Int, y: Int): GameCell {
        val cell = GameCell(context, layout, size, x, y)
        cell.getView().setOnClickListener {

        }
        return cell
    }

    fun setShips(coors: Array<Int>) {
        ships = emptyArray()
        val count = coors.size / 4
        for (i in 0 until count) {
            val x = (coors[i * 4]) * size + left
            val y = (coors[i * 4 + 1]) * size + top
            val len = coors[i * 4 + 2]
            val ver = coors[i * 4 + 3]
            val ship = GameShipPlayer(context, layout, size, x, y, len, ver == 1)
            ships += ship
            for (j in 0 until ship.getLength()) {
                cells2[(ship.getCells()[j].first - left) / size][(ship.getCells()[j].second - top) / size] =
                    1
            }
        }
    }

    fun boom(x: Int, y: Int): Array<Any> {
        val flag = (cells2[x][y] > 0)
        var flag2 = false
        var len = 0
        createBoom(flag, x, y)

        for (ship in ships) {
            if (Pair(x * size + left, y * size + top) in ship.getCells()) {
                ship.boom(x * size + left, y * size + top)
                flag2 = ship.isDestroyed()
                len = ship.getLength()
                if (flag2){
                    createBigBoom(ship)
                }
            }
        }
        var lst = emptyArray<Any>()
        lst += flag
        lst += flag2
        lst += len
        printField()
        cells2[x][y] = -1
        //Log.w("cell", "$flag $flag2")
        return lst
    }

    private fun printField() {
        var ans = "||\n"
        for (i in 0..9) {
            for (j in 0..9) {
                ans += cells2[i][j].toString() + "\t"
            }
            ans += "\n"
        }
        //Log.i("cells", ans)
    }

    private fun createBigBoom(ship: GameShipPlayer) {
        val p = ship.getCells()
        var a = emptyArray<Int>()
        var b = emptyArray<Int>()
        for (e in p) {
            a += e.first
            b += e.second
        }
        var right = (a.max() - left) / size + 1
        var l = (a.min() - left) / size - 1
        var bottom = (b.max() - top) / size + 1
        var t = (b.min() - top) / size - 1
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
            background.setColor(ContextCompat.getColor(context, R.color.red))
        } else {
            background.setColor(ContextCompat.getColor(context, R.color.grey))
        }
        boomView.background = background
        boomView.setTextColor(ContextCompat.getColor(context, R.color.black_orange))
        boomView.gravity = Gravity.CENTER

        val params = ConstraintLayout.LayoutParams(size, size)
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftMargin = left + x * size
        params.topMargin = top + y * size
        boomView.layoutParams = params
        layout.addView(boomView)
    }
}