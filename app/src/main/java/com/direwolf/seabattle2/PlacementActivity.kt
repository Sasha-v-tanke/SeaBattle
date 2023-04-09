package com.direwolf.seabattle2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.direwolf.seabattle2.objects.*
import kotlin.math.min

class PlacementActivity : DefaultActivity() {
    private lateinit var field: Array<Array<Cell>>
    private lateinit var grid1: GridLayout
    private lateinit var grid2: GridLayout
    private var ships = arrayListOf<Ship>()
    private lateinit var cells: Array<Array<NoneCell>>
    private var cellSize1: Int = 0
    private var cellSize2: Int = 0
    private val defaultPlace = listOf(4, 9)
    private lateinit var selectedShip: Ship

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placement)

        val continueButton = findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {
            val flag = checkPlacement()
            if (flag){
                val intent = Intent(this, StartActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        grid1 = findViewById(R.id.grid1)
        grid2 = findViewById(R.id.grid2)

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val screenWidth = displayMetrics.widthPixels
        cellSize1 = min(screenHeight / 12, screenWidth / 2 / 11)
        cellSize2 = min(screenHeight / 11, screenWidth / 2 / 16)

        setMargins()
        createCells()
        setShips()
    }

    private fun setShips(){
        val coords = arrayListOf<List<Int>>()
        coords.add(listOf(1, 1, 4))
        coords.add(listOf(1, 6, 2))

        coords.add(listOf(3, 1, 3))
        coords.add(listOf(3, 5, 3))

        coords.add(listOf(5, 1, 1))
        coords.add(listOf(5, 3, 1))
        coords.add(listOf(5, 5, 1))
        coords.add(listOf(5, 7, 1))

        coords.add(listOf(7, 1, 2))
        coords.add(listOf(7, 4, 2))

        val coords2 = arrayListOf<List<Int>>()
        for (coor in coords){
            coords2.add(listOf(coor[0], coor[1]))
        }

        for (x in 0..19){
            for (y in 0..19){
                if (listOf(x, y) in  coords2){
                    for (i in 0..5){
                        if (listOf(x, y, i) in  coords){
                            val ship = Ship1(this, grid2, i, x, y, cellSize2)
                            ship.getView().setOnClickListener{
                                selectShip(ship)
                            }
                            ships.add(ship)
                            break
                        }
                    }
                }
            }
        }
        //Log.w("my app", defaultPlace.toString())
        selectedShip = SelectedShip(this, grid2, 4, defaultPlace[0],
            defaultPlace[1], cellSize2, false)
        selectedShip.getView().setOnClickListener {
            rotateShip()
        }
    }

    private fun selectShip(ship: Ship1){
        val length = ship.getLength()
        grid2.removeView(selectedShip.getView())
        selectedShip = SelectedShip(this, grid2, length, defaultPlace[0],
            defaultPlace[1], cellSize2, false)
        selectedShip.getView().setOnClickListener {
            rotateShip()
        }
    }
    private fun rotateShip(){
        val vertical = !selectedShip.getOrientation()
        val len = selectedShip.getLength()
        grid2.removeView(selectedShip.getView())
        selectedShip = SelectedShip(this, grid2, len, defaultPlace[0],
            defaultPlace[1], cellSize2, vertical)
        selectedShip.getView().setOnClickListener {
            rotateShip()
        }
    }

    private fun checkPlacement(): Boolean{
        return false
    }

    private fun setMargins(){
        var layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.leftMargin = cellSize1
        layoutParams.width = cellSize1 * 10
        layoutParams.height = cellSize1 * 10
        grid1.layoutParams = layoutParams

        layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.rightMargin = cellSize2
        layoutParams.width = cellSize2 * 15
        layoutParams.height = cellSize2 * 9
        grid2.layoutParams = layoutParams
    }

    private fun createCells(){
        field = Array(10) { i ->
            Array(10) { j ->
                Cell(this, grid1, i, j, cellSize1)
            }
        }
        cells = Array(9) { i ->
            Array(15) { j ->
                NoneCell(this, grid2, i, j, cellSize2)
            }
        }
    }
}