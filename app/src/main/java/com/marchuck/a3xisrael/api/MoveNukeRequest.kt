package com.marchuck.a3xisrael.api

import com.google.gson.annotations.SerializedName

data class MoveNukeRequest(
        @field:SerializedName("index") val index: Int = 0,
        @field:SerializedName("direction") val direction: NukeDirection
)