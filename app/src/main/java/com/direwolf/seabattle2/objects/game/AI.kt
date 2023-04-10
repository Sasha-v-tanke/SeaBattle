package com.direwolf.seabattle2.objects.game

import android.util.Log

class AI {
    private var selfField: Array<Array<Int>> = Array(10) { Array(10) { 0 } }
    private var enemyField: Array<Array<Int>> = Array(10) { Array(10) { 0 } }

    fun setShips():  Array<Int> {
        var coords:  Array<Int>
        val ships = listOf(1, 2, 3, 4)
        while (true) {
            coords = emptyArray()
            for (i in 0 until ships.size){
                for (n in 0 until i + 1){
                    val res = setShip(4 - i)
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

    private fun setShip(length: Int): List<Any> {
        Log.w("ai", "0")
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

    private fun checkPlace(x: Int, y: Int, length: Int, vertical: Boolean): Boolean {
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
        if (flag){
            Log.w("ship", "$x $y $length $vertical")
        }
        return flag
    }


    /*
    def set_ships(self):
        while True:
            ship_cells = []
            number = 1
            for type_ship in self.self_ships.keys():
                for count in range(self.self_ships[type_ship]):
                    ship_cells.append(self.set_one_ship(number, type_ship))
                    number += 1
            if self.checkPlacement():
                break
            else:
                self.self_field.reset()
        return ship_cells
     */

    private fun reset() {
        selfField = Array(10) { Array(10) { 0 } }
    }

    fun boom(): Pair<Int, Int> {
        return Pair(0, 0)
    }
}