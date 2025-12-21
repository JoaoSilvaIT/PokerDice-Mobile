package com.pdm.pokerdice.domain.lobby


data class LobbyDto(
    val id: Int,
    val name: String?,
    val description: String?,
    val minPlayers: Int,
    val maxPlayers: Int,
    val players: List<LobbyPlayerDto?>?,
    val hostId: Int
) {
    fun toDomain(): Lobby {
        val safePlayers = players?.filterNotNull() ?: emptyList()
        val host = safePlayers.find { it.id == hostId } ?: LobbyPlayerDto(hostId, "Unknown")
        return Lobby(
            id = id,
            name = name ?: "Unknown Lobby",
            description = description ?: "",
            settings = LobbySettings(
                numberOfRounds = 5, // Default as not present in DTO
                minPlayers = minPlayers,
                maxPlayers = maxPlayers
            ),
            players = safePlayers.map { it.toDomain() }.toSet(),
            host = host.toDomain()
        )
    }
}