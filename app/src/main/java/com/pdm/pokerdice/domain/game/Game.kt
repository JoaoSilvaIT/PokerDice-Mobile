package com.pdm.pokerdice.domain.game

import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.domain.game.utilis.State

data class Game (
    val id: Int,
    val lobbyId: Int,
    val players: List<PlayerInGame>,
    val numberOfRounds: Int,
    val state: State,
    val currentRound: Round?,
    val startedAt: Long,
    val endedAt: Long?,
    // List to register the gains of each user in each round to decide the final winner
    val gameGains: List<Pair<PlayerInGame, Int>> = emptyList(),
)