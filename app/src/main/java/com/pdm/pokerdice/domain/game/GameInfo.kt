package com.pdm.pokerdice.domain.game

import com.pdm.pokerdice.domain.lobby.Lobby

data class GameInfo(
    val numberOfRounds: Int,
    val lobby: Lobby,
)
