package com.direwolf.seabattle2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.direwolf.seabattle2.objects.*
import kotlin.math.min

class PlacementActivity : DefaultActivity() {
    private var screenWidth = 0
    private var screenHeight = 0
    private var cellSize1: Int = 0
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
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val layout = findViewById<ConstraintLayout>(R.id.layout)

        val displayMetrics = resources.displayMetrics
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels

        cellSize1 = min(screenHeight / 12, screenWidth / 25)

        val top = screenHeight / 2 - 5 * cellSize1
        grid = PlacementGrid(this, layout, cellSize1, 10, 10, cellSize1, top)

        setShips()
    }

    private fun setShips() {
        var left = screenWidth - 2 * cellSize1
        var top = cellSize1
        createShip(left, top, 4)
        left = screenWidth - 4 * cellSize1
        createShip(left, top, 3)
        left = screenWidth - 6 * cellSize1
        createShip(left, top, 3)
        left = screenWidth - 8 * cellSize1
        createShip(left, top, 1)
        left = screenWidth - 2 * cellSize1
        top = cellSize1 * 6
        createShip(left, top, 2)
        left = screenWidth - 4 * cellSize1
        createShip(left, top, 2)
        left = screenWidth - 6 * cellSize1
        createShip(left, top, 2)
        top = cellSize1 * 5
        left = screenWidth - 8 * cellSize1
        createShip(left, top, 1)
        top = cellSize1 * 3
        left = screenWidth - 8 * cellSize1
        createShip(left, top, 1)
        top = cellSize1 * 7
        left = screenWidth - 8 * cellSize1
        createShip(left, top, 1)
    }

    private fun createShip(left: Int, top: Int, length: Int) {
        val x = screenWidth - 11 * cellSize1
        val y = cellSize1 * 6
        val defaultPlace = Pair(x, y)
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        ships.add(PlacementShip(this, layout, cellSize1, left, top, length, defaultPlace))
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

    fun shipUnselect(){
        selectedShip = null
    }

    fun getInf(): List<Any?> {
        val left = cellSize1
        val right = left + cellSize1 * 10
        val top = screenHeight / 2 - 5 * cellSize1
        val bottom = top + cellSize1 * 10
        return listOf(selectedShip, left, right, top, bottom)
    }

    fun setShip(x: Int, y: Int) {
        if (selectedShip != null) {
            val length = selectedShip!!.getLength()
            val left = cellSize1
            val right = left + cellSize1 * 10
            val top = screenHeight / 2 - 5 * cellSize1
            val bottom = top + cellSize1 * 10
            if (selectedShip!!.getOrientation()) {
                if ((left <= x) and (x <= right)) {
                    if ((top <= y) and (y + cellSize1 * length <= bottom)) {
                        selectedShip!!.set(x, y)
                        selectedShip = null
                    }
                }
            }
            else{
                if ((left <= x) and (x + cellSize1 * length <= right)) {
                    if ((top <= y) and (y <= bottom)) {
                        selectedShip!!.set(x, y)
                        selectedShip = null
                    }
                }
            }
        }
    }
}