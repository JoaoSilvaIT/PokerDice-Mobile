package com.pdm.pokerdice.domain.lobby

/**
 * Represents the countdown state of a lobby
 */
data class LobbyCountdown(
    val lobbyId: Int,
    val expiresAt: Long,
    val remainingSeconds: Long,
) {
    companion object {
        fun fromExpiresAt(
            lobbyId: Int,
            expiresAt: Long,
        ): LobbyCountdown {
            val now = System.currentTimeMillis()
            val remaining = ((expiresAt - now) / 1000).coerceAtLeast(0)
            return LobbyCountdown(lobbyId, expiresAt, remaining)
        }
    }

    fun updateRemaining(): LobbyCountdown {
        val now = System.currentTimeMillis()
        val remaining = ((expiresAt - now) / 1000).coerceAtLeast(0)
        return copy(remainingSeconds = remaining)
    }

    val isExpired: Boolean get() = remainingSeconds <= 0
}
