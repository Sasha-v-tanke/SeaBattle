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

class PlacementGrid (private val context: Context, private val layout: ConstraintLayout,
                     private val size: Int,
                     x: Int, y: Int,
                     private val left: Int, private val top: Int) {
    private var cells: Array<Array<PlacementCell>>
    private var cells2 = Array(x) { Array(y) { 0 } }

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
            setShip(x, y)
        }
        return cell
    }

    private fun setShip(x: Int, y: Int) {
        val lst = (context as PlacementActivity).getInf()
        val selectedShip = lst[0] as PlacementShip?
        if (selectedShip != null) {
            val length = selectedShip.getLength()
            val left = lst[1] as Int
            val right = lst[2] as Int
            val top = lst[3] as Int
            val bottom = lst[4] as Int
            if (selectedShip.getOrientation()) {
                if ((left <= x) and (x <= right)) {
                    if ((top <= y) and (y + size * length <= bottom)) {
                        val i = (x - left) / size
                        val j = (y - top) / size
                        val flag = checkPlace(i, j, length, true)
                        if (flag) {
                            selectedShip.set(x, y)
                            for (n in 0 until length) {
                                cells2[i][j + n] = 1
                            }
                            context.shipUnselect()
                        }
                    }
                }
            } else {
                if ((left <= x) and (x + size * length <= right)) {
                    if ((top <= y) and (y <= bottom)) {
                        val i = (x - left) / size
                        val j = (y - top) / size
                        val flag = checkPlace(i, j, length, false)
                        if (flag) {
                            selectedShip.set(x, y)
                            for (n in 0 until length) {
                                cells2[i + n][j] = 1
                            }
                            context.shipUnselect()
                        }
                    }
                }
            }
        }
    }

    private fun checkPlace(x: Int, y: Int, len: Int, vertical: Boolean): Boolean {
        if (vertical) {
            var flag = true
            for (m in -1..1) {
                for (n in -1..len) {
                    if (((0 <= x + m) and (x + m <= 9)) and ((0 <= y + n) and (y + n <= 9))) {
                        if (cells2[x + m][y + n] != 0) {
                            flag = false
                            break
                        }
                    }
                }
            }
            return flag
        } else {
            var flag = true
            for (m in -1..1) {
                for (n in -1..len) {
                    if (((0 <= x + n) and (x + n <= 9)) and ((0 <= y + m) and (y + m <= 9))) {
                        if (cells2[x + n][y + m] != 0) {
                            flag = false
                            break
                        }
                    }
                }
            }
            return flag
        }
    }

    fun checkPlacement(): Boolean {
        var count = 0
        for (i in 0..9) {
            for (j in 0..9) {
                if (cells2[i][j] != 0) {
                    count++
                }
            }
        }
        return count == 20
    }

    fun removeShip(x: Int, y: Int, len: Int, vertical: Boolean) {
        val i = (x - left) / size
        val j = (y - top) / size
        Log.w("ship", "$i $j $x $y")
        for (n in 0 until len) {
            if (vertical) {
                cells2[i][j + n] = 0
            } else {
                cells2[i + n][j] = 0
            }
        }
    }

    fun getCells(): Array<Array<Int>> {
        return cells2
    }
}