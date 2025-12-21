package com.pdm.pokerdice.domain.user

data class UserStatisticsDto(
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Double
) {
    fun toDomain() = UserStatistics(gamesPlayed, wins, losses, winRate)
}