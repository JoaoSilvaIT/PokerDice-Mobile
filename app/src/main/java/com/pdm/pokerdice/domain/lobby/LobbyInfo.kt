package com.pdm.pokerdice.domain.lobby

data class LobbyInfo(
    val name: String,
    val description : String,
    val expectedPlayers : Int,
    val rounds : Int
)
