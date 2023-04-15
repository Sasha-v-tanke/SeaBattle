package com.direwolf.seabattle2.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.direwolf.seabattle2.R
import com.direwolf.seabattle2.objects.placement.PlacementGrid
import com.direwolf.seabattle2.objects.placement.PlacementShip
import kotlin.math.min

class PlacementActivity : DefaultActivity() {
    private var cellSize: Int = 0
    private lateinit var grid: PlacementGrid
    private val ships = arrayListOf<PlacementShip>()
    private var selectedShip: PlacementShip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)

        val continueButton = findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {
            val flag = checkPlacement()
            if (flag) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("grid", packShips())
                startActivity(intent)
            }
        }

        val autoButton = findViewById<Button>(R.id.autoButton)
        autoButton.setOnClickListener {
            autoSet()
        }

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            for (ship in ships){
                if (ship.isSelected()){
                    ship.select()
                }
            }
            selectedShip?.unselect()
        }
    }

    private fun packShips(): Array<Int> {
        var shipsCoor = emptyArray<Int>()
        for (ship in ships) {
            val coor = ship.getCoor()
            val x = (coor.first - getInf()[1] as Int) / cellSize
            val y = (coor.second - getInf()[3] as Int) / cellSize
            val len = ship.getLength()
            val ver = if (ship.getOrientation()) {
                1
            } else {
                0
            }
            shipsCoor += x
            shipsCoor += y
            shipsCoor += len
            shipsCoor += ver
        }
        return shipsCoor
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val layout = findViewById<ConstraintLayout>(R.id.layout)

        cellSize = min(screenHeight / 12, screenWidth / 25)

        val top = screenHeight / 2 - 5 * cellSize
        grid = PlacementGrid(this, layout, cellSize, 10, 10, cellSize, top)

        setShips()
    }

    private fun setShips() {
        var left = screenWidth - 2 * cellSize
        var top = cellSize
        createShip(left, top, 4)
        left = screenWidth - 4 * cellSize
        createShip(left, top, 3)
        left = screenWidth - 6 * cellSize
        createShip(left, top, 3)
        left = screenWidth - 2 * cellSize
        top = cellSize * 6
        createShip(left, top, 2)
        left = screenWidth - 4 * cellSize
        createShip(left, top, 2)
        left = screenWidth - 6 * cellSize
        createShip(left, top, 2)
        top = cellSize * 5
        left = screenWidth - 8 * cellSize
        createShip(left, top, 1)
        top = cellSize * 3
        left = screenWidth - 8 * cellSize
        createShip(left, top, 1)
        top = cellSize * 7
        left = screenWidth - 8 * cellSize
        createShip(left, top, 1)
        top = cellSize
        left = screenWidth - 8 * cellSize
        createShip(left, top, 1)

    }

    fun removeShip(ship: PlacementShip) {
        val coor = ship.getCoor()
        grid.removeShip(coor.first, coor.second, ship.getLength(), ship.getOrientation())
    }

    private fun createShip(left: Int, top: Int, length: Int) {
        val x = screenWidth - 11 * cellSize
        val y = cellSize * 6
        val defaultPlace = Pair(x, y)
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        ships.add(PlacementShip(this, layout, cellSize, left, top, length, defaultPlace))
    }

    private fun checkPlacement(): Boolean {
        return grid.checkPlacement()
    }

    fun shipSelect(ship: PlacementShip) {
        if (selectedShip != null) {
            selectedShip!!.unselect()
        }
        selectedShip = ship
    }

    fun shipUnselect() {
        selectedShip = null
    }

    fun getInf(): List<Any?> {
        val left = cellSize
        val right = left + cellSize * 10
        val top = screenHeight / 2 - 5 * cellSize
        val bottom = top + cellSize * 10
        return listOf(selectedShip, left, right, top, bottom)
    }

    fun autoSet(){
        for (ship in ships){
            if (ship.isSelected()){
                ship.select()
            }
        }
        selectedShip?.unselect()

        val selfField = Array(10) { Array(10) { 0 } }
        fun checkPlace(x: Int, y: Int, length: Int, vertical: Boolean): Boolean {
            var flag = true
            if (vertical){
                if (y + length - 1 > 9){
                    return false
                }
            }
            else{
                if (x + length - 1 > 9){
                    return false
                }
            }
            if (vertical) {
                flag = true
                for (m in -1..1) {
                    for (n in -1..length) {
                        if (((0 <= x + m) and (x + m <= 9)) and ((0 <= y + n) and (y + n <= 9))) {
                            if (selfField[x + m][y + n] != 0) {
                                flag = false
                                break
                            }
                        }
                    }
                }
            } else {
                flag = true
                for (m in -1..1) {
                    for (n in -1..length) {
                        if (((0 <= x + n) and (x + n <= 9)) and ((0 <= y + m) and (y + m <= 9))) {
                            if (selfField[x + n][y + m] != 0) {
                                flag = false
                                break
                            }
                        }
                    }
                }
            }
            return flag
        }
        fun setShip2(length: Int): List<Any> {
            var flag: Boolean
            while (true) {
                flag = true

                val x_start = (0..9).random()
                val y_start = (0..9).random()

                val direction = (-1..0).random()

                for (i in 0 until length) {
                    val x_cell = x_start - i * direction
                    val y_cell = y_start + i * (direction + 1)
                    if ((x_cell !in 0..9) or (y_cell !in 0..9)) {
                        flag = false
                        break
                    }
                }
                if (flag) {

                    if (checkPlace(x_start, y_start, length, direction == 0)) {
                        for (i in 0 until length) {
                            if (direction == 0) {
                                selfField[x_start][y_start + i] = 1
                            }
                            else{
                                selfField[x_start + i][y_start] = 1
                            }
                        }
                        return listOf(x_start, y_start, direction == 0)
                    }
                }
            }
        }
        fun setShips2(): Array<Int> {
            var coords: Array<Int>
            val ships2 = listOf(1, 2, 3, 4)
            while (true) {
                coords = emptyArray()
                for (i in ships2.indices) {
                    for (n in 0 until i + 1) {
                        val res = setShip2(4 - i)
                        coords += res[0] as Int
                        coords += res[1] as Int
                        coords += 3 - i
                        coords += if (res[2] as Boolean) 1 else 0
                    }
                }
                break
            }
            return coords
        }

        val coords = setShips2()
        for (i in 0..9){
            val newShip = ships[i]
            newShip.select()
            val x = (coords[i * 4]) * cellSize + getInf()[1] as Int
            val y = (coords[i * 4 + 1]) * cellSize + getInf()[3] as Int
            val len = coords[i * 4 + 2] + 1
            val ver = coords[i * 4 + 3]
            if (ver == 0){
                selectedShip!!.rotate()
            }
            val a = selectedShip!!.getOrientation()
            grid.setShip(x, y)
        }
    }
}