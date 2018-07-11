package com.marchuck.a3xisrael.moveNuke

import com.marchuck.a3xisrael.api.NukeDirection

interface MoveNukeListener {
    fun onMoveNuke(direction: NukeDirection)
}