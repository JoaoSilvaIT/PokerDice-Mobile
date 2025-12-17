package com.pdm.pokerdice.domain.game

import com.pdm.pokerdice.domain.user.User

const val MAX_ROLLS = 3

data class Turn(
    val player: User,
    val rollsRemaining: Int,
    val currentDice: List<Dice> = emptyList(),
    val finalHand: Hand = Hand(emptyList()),
)
