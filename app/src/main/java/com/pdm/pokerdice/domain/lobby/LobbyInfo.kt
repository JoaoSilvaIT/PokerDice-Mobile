package com.pdm.pokerdice.domain.lobby

data class LobbyInfo(
    val name: String,
    val description : String,
    val minPlayers : Int,
    val maxPlayers : Int,
    val rounds : Int
)
