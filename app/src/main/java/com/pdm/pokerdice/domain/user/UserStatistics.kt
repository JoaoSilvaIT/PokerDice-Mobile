package com.pdm.pokerdice.domain.user

data class UserStatistics(
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Double,
)