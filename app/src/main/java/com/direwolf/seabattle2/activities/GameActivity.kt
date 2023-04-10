package com.direwolf.seabattle2.activities

import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.direwolf.seabattle2.R
import com.direwolf.seabattle2.objects.game.AI
import com.direwolf.seabattle2.objects.game.AIGrid
import com.direwolf.seabattle2.objects.game.PlayerGrid
import java.lang.Integer.min

class GameActivity : DefaultActivity() {
    private lateinit var playerGrid: PlayerGrid
    private lateinit var aiGrid: AIGrid
    private lateinit var ai: AI
    private var playerTurn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val layout = findViewById<ConstraintLayout>(R.id.layoutt)
        val cellSize = min(screenHeight / 12, screenWidth / 23)

        val top = screenHeight / 2 - 5 * cellSize
        playerGrid = PlayerGrid(this, layout, cellSize, 10, 10, cellSize, top)
        aiGrid = AIGrid(
            this,
            layout,
            cellSize,
            10,
            10,
            screenWidth - cellSize * 10,
            top,
            ::playerListener
        )
        ai = AI()
        val ships1 = ai.setShips()
        Log.w("cells", ships1.toString())
        aiGrid.setShips(ships1)

        val ships2 = intent.extras?.get("grid") as Array<Int>
        playerGrid.setShips(ships2)
    }

    private fun playerListener(x: Int, y: Int) {
        if (playerTurn) {
            var res = aiGrid.boom(x, y)
            Log.w("boom", "$x $y")
            if (res) {
                if (aiGrid.check()) {
                    endGame(true)
                }
            } else {
                playerTurn = false
                var coor = ai.boom()
                res = playerGrid.boom(coor.first, coor.second)

                while (res) {
                    if (playerGrid.check()) {
                        endGame(false)
                    }
                    coor = ai.boom()
                    res = playerGrid.boom(coor.first, coor.second)
                }
                if (playerGrid.check()) {
                    endGame(false)
                }
                playerTurn = true
            }
        }
    }

    private fun endGame(player: Boolean) {

    }
}