package com.direwolf.seabattle2.objects.game

import android.util.Log

class AI {
    private var selfField: Array<Array<Int>> = Array(10) { Array(10) { 0 } }
    private var enemyField: Array<Array<Int>> = Array(10) { Array(10) { 0 } }
    private var lastShot: Pair<Int, Int> = Pair(-1, -1)
    private var enemyShips = intArrayOf(4, 3, 2, 1)
    fun setShips(): Array<Int> {
        var coords: Array<Int>
        val ships = listOf(1, 2, 3, 4)
        while (true) {
            coords = emptyArray()
            for (i in ships.indices) {
                for (n in 0 until i + 1) {
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
        //Log.w("ai", "0")
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
                        } else {
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
        if (vertical) {
            if (y + length - 1 > 9) {
                return false
            }
        } else {
            if (x + length - 1 > 9) {
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
        if (flag) {
            //Log.w("ship", "$x $y $length $vertical")
        }
        return flag
    }

    private fun reset() {
        selfField = Array(10) { Array(10) { 0 } }
    }

    fun boom(): Pair<Int, Int> {
        val coef = setCoef()
        lastShot = getMaxSum(coef)
        return lastShot
    }

    private fun setCoef(): Array<Array<Int>> {
        val coef = Array(10) { Array(10) { 0 } }
        for (x in 0..9){
            for (y in 0..9){
                if (enemyField[x][y] == 0){
                    coef[x][y] = 1
                }
            }
        }

        for (type in 1..4){
            if (enemyShips[type - 1] <= 0){
                continue
            }
            for (x in 0 until 11 - type){
                for (y in 0..9){
                    if (checkPlace2(x, y, false, type)){
                        for (a in 1..type){
                            if (enemyField[x - 1 + a][y] == 0) {
                                coef[x - 1 + a][y] += 1
                            }
                        }
                    }
                    if (checkPlace2(y, x,true,  type)){
                        for (a in 1..type){
                            if (enemyField[y][x - 1 + a] == 0) {
                                coef[y][x - 1 + a] += 1
                            }
                        }
                    }
                }
            }
        }

        for (x in 0..9){
            for (y in 0..9){
                if (enemyField[x][y]!= 1){
                    continue
                }
                for (type in 1..4){
                    if (enemyShips[type - 1] <= 0){
                        continue
                    }
                    for (a in 1-type..0){
                        if (checkPlace2(x + a, y, false, type)){
                            for (b in 1..type){
                                if (enemyField[x + b - 1 + a][y] == 0){
                                    coef[x + b - 1 + a][y] += 90
                                }
                            }
                        }
                        if (checkPlace2(x, y + a, true, type)){
                            for (b in 1..type){
                                if (enemyField[x][y + b - 1 + a] == 0){
                                    coef[x][y + b - 1 + a] += 90
                                }
                            }
                        }
                    }
                }
            }
        }


        return coef
    }
    private fun checkPlace2(x: Int, y: Int, vertical: Boolean, length: Int): Boolean {
        if (x !in 0..9 || y !in 0..9){
            return false
        }
        if (vertical){
            if (y + length > 9){
                return false
            }
        }
        else {
            if (x + length > 9){
                return false
            }
        }
        for (a in 0 until length) {
            if (vertical) {
                if (y + a > 9) {
                    return false
                }
                if (enemyField[x][y + a] == -1) {
                    return false
                }
            } else {
                if (x + a > 9) {
                    return false
                }
                if (enemyField[x + a][y] == -1) {
                    return false
                }
            }
        }
        return true
    }

    private fun printCoef(coef: Array<Array<Int>>) {
        var ans = "||\n"
        for (i in 0..9) {
            for (j in 0..9) {
                ans += coef[i][j].toString() + " \t \t"
            }
            ans += "\n"
        }
        //Log.i("coef", ans)
    }

    private fun printField() {
        var ans = "||\n"
        for (i in 0..9) {
            for (j in 0..9) {
                ans += enemyField[i][j].toString() + " \t \t"
            }
            ans += "\n"
        }
        //Log.i("field", ans)
    }

    private fun getMaxSum(coef: Array<Array<Int>>): Pair<Int, Int> {
        var maxSum = 0
        var lst = emptyArray<Pair<Int, Int>>()
        for (x in 0..9) {
            for (y in 0..9) {
                if (coef[x][y] == maxSum) {
                    lst += Pair(x, y)
                } else if (coef[x][y] > maxSum) {
                    lst = emptyArray()
                    lst += Pair(x, y)
                    maxSum = coef[x][y]
                }
            }
        }
        return lst.random()
    }

    fun setResult(result: Boolean, x: Int, y: Int, destroy: Boolean, len: Int) {
        if (result) {
            enemyField[x][y] = 1

            if (x >= 1 && y >= 1) {
                enemyField[x - 1][y - 1] = -1
            }
            if (x >= 1 && y < 9) {
                enemyField[x - 1][y + 1] = -1
            }
            if (x < 9 && y >= 1) {
                enemyField[x + 1][y - 1] = -1
            }
            if (x < 9 && y < 9) {
                enemyField[x + 1][y + 1] = -1
            }

            fun f(x: Int, y: Int) {
                enemyField[x][y] = -1
                if (x + 1 < 10) {
                    if (enemyField[x + 1][y] == 1) {
                        f(x + 1, y)
                    } else {
                        enemyField[x + 1][y] = -1
                    }
                }
                if (x - 1 >= 0) {
                    if (enemyField[x - 1][y] == 1) {
                        f(x - 1, y)
                    } else {
                        enemyField[x - 1][y] = -1
                    }
                }
                if (y + 1 < 10) {
                    if (enemyField[x][y + 1] == 1) {
                        f(x, y + 1)
                    } else {
                        enemyField[x][y + 1] = -1
                    }
                }
                if (y - 1 >= 0) {
                    if (enemyField[x][y - 1] == 1) {
                        f(x, y - 1)
                    } else {
                        enemyField[x][y - 1] = -1
                    }
                }
            }

            if (destroy) {
                f(x, y)
                enemyShips[len - 1] -= 1
            }
        } else {
            enemyField[x][y] = -1
        }
        printField()
    }
}