package com.pdm.pokerdice.domain.game

import com.pdm.pokerdice.domain.game.utilis.State

data class GameDto(
    val id: Int,
    val startedAt: Long,
    val endedAt: Long?,
    val lobbyId: Int?,
    val numberOfRounds: Int,
    val state: String?,
    val currentRound: GameRoundDto?,
    val players: List<PlayerInGameDto?>?,
) {
    fun toDomain(currentUserId: Int): Game {
        val gameState =
            try {
                State.valueOf(state ?: "RUNNING")
            } catch (e: Exception) {
                State.RUNNING
            }
        val safePlayers = players?.filterNotNull() ?: emptyList()
        val domainPlayers = safePlayers.map { it.toDomain() }

        // Map current round if exists
        val domainRound = currentRound?.toDomain(domainPlayers)

        return Game(
            id = id,
            lobbyId = lobbyId,
            players = domainPlayers,
            numberOfRounds = numberOfRounds,
            state = gameState,
            currentRound = domainRound,
            startedAt = startedAt,
            endedAt = endedAt,
        )
    }
}
