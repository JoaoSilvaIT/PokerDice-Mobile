package com.pdm.pokerdice.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStatistics(
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Double,
): Parcelable