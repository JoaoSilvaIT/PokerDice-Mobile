package com.pdm.pokerdice.domain.user

import com.pdm.pokerdice.domain.game.utilis.HandRank

data class UserStatisticsDto(
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Double,
    val handFrequencies: Map<String, Int> = emptyMap(),
) {
    fun toDomain() =
        UserStatistics(
            gamesPlayed,
            wins,
            losses,
            winRate,
            handFrequencies
                .mapNotNull { (key, value) ->
                    try {
                        HandRank.valueOf(key) to value
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }.toMap(),
        )
}
