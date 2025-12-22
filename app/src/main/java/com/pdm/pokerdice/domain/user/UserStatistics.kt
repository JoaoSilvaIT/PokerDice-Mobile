package com.pdm.pokerdice.domain.user

import android.os.Parcelable
import com.pdm.pokerdice.domain.game.utilis.HandRank
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStatistics(
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Double,
    val handFrequencies: Map<HandRank, Int> = emptyMap()
) : Parcelable
