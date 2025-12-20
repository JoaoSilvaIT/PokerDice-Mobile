package com.pdm.pokerdice.domain.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val MAX_ROLLS = 3

@Parcelize
data class Turn(
    val player: PlayerInGame,
    val rollsRemaining: Int,
    val currentDice: List<Dice> = emptyList(),
    val finalHand: Hand = Hand(emptyList()),
    val isFolded: Boolean = false,
) : Parcelable
