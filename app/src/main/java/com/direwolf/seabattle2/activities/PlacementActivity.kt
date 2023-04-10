package com.direwolf.seabattle2.activities

import android.content.Intent
import android.os.Bundle
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
        left = screenWidth - 8 * cellSize
        createShip(left, top, 1)
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
}