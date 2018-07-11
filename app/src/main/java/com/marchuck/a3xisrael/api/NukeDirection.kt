package com.marchuck.a3xisrael.api

import com.google.gson.annotations.SerializedName

data class NukeDirection(
        @field:SerializedName("x") val x: Int = 0,
        @field:SerializedName("y") val y: Int = 0
)