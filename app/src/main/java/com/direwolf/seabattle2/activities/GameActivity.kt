package com.direwolf.seabattle2.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    private var shotsPlayer = 0
    private var shotsAI = 0

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
        aiGrid.setShips(ships1)

        val ships2 = intent.extras?.get("grid") as Array<Int>
        playerGrid.setShips(ships2)
    }

    private fun playerListener(x: Int, y: Int) {
        if (playerTurn) {
            var res = aiGrid.boom(x, y)
            //Log.w("boom", "$x $y $res")
            if (res.first) {
                shotsPlayer += 1
                if (shotsPlayer == 20) {
                    Log.w("end", "player")
                    endGame(true)
                }
            } else {
                playerTurn = false
                var coor = ai.boom()
                var res2 = playerGrid.boom(coor.first, coor.second)
                Log.w("bot", "${coor.first} ${coor.second}")
                //Log.w("bot", "${coor.first} ${coor.second} ${res2[0]} ${res2[1]} ${res2[2]}")
                ai.setResult(
                    res2[0] as Boolean,
                    coor.first,
                    coor.second,
                    res2[1] as Boolean,
                    res2[2] as Int
                )
                while (res2[0] as Boolean) {
                    shotsAI += 1
                    if (shotsAI == 20) {
                        Log.w("end", "ai")
                        endGame(false)
                        break
                    }
                    coor = ai.boom()
                    Log.w("bot", "${coor.first} ${coor.second}")
                    res2 = playerGrid.boom(coor.first, coor.second)
                    //Log.w("bot", "${coor.first} ${coor.second} ${res2[0]} ${res2[1]} ${res2[2]}")
                    ai.setResult(
                        res2[0] as Boolean,
                        coor.first,
                        coor.second,
                        res2[1] as Boolean,
                        res2[2] as Int
                    )
                }
                playerTurn = true
            }
        }
    }

    private fun endGame(player: Boolean) {
        val text: String
        if (player) {
            text = "Player win!"
        }
        else{
            text = "AI win!"
        }
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }
}