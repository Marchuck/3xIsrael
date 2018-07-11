package com.marchuck.a3xisrael.moveNuke

import com.marchuck.a3xisrael.api.NukeDirection

enum class BombDirection(val x: Int, val y: Int) {

    TOP(0, 1),
    LEFT(-1, 0),
    BOTTOM(0, -1),
    RIGHT(1, 0);

    fun asNukeDirection() = NukeDirection(x, y)
}