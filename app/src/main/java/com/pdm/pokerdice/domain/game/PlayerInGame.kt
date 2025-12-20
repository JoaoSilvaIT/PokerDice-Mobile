package com.pdm.pokerdice.domain.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerInGame(
    val id: Int,
    val name: String,
    val currentBalance: Int,
    // val isActive: Boolean, // false se não conseguir pagar ante
    val moneyWon: Int,
    val handRank: String? = null
) : Parcelable
