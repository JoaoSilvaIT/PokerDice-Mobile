package com.pdm.pokerdice.domain.lobby

import com.pdm.pokerdice.domain.lobby.LobbyCountdown

/**
 * DTOs for SSE (Server-Sent Events) from the backend
 */

data class CountdownStartedDto(
    val lobbyId: Int,
    val expiresAt: Long,
) {
    fun toDomain(): LobbyCountdown = LobbyCountdown.fromExpiresAt(lobbyId, expiresAt)
}

data class GameStartedDto(
    val lobbyId: Int,
    val gameId: Int,
)

/**
 * Union type for all possible SSE events
 */
sealed class LobbyEvent {
    data class CountdownStarted(
        val countdown: LobbyCountdown,
    ) : LobbyEvent()

    data class GameStarted(
        val lobbyId: Int,
        val gameId: Int,
    ) : LobbyEvent()

    data class LobbyUpdated(
        val lobby: Lobby,
    ) : LobbyEvent()

    data object ConnectionLost : LobbyEvent()
}
