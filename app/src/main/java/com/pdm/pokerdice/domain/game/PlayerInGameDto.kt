package com.pdm.pokerdice.domain.game

import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserStatistics

data class PlayerInGameDto(
    val id: Int,
    val name: String?,
    val currentBalance: Int,
    val moneyWon: Int,
    val handRank: String?,
) {
    fun toDomain() = PlayerInGame(id, name ?: "Unknown", currentBalance, moneyWon, handRank)

    fun toDomainUser() = User(id, name ?: "Unknown", "", "", currentBalance, UserStatistics(0, 0, 0, 0.0))
}
