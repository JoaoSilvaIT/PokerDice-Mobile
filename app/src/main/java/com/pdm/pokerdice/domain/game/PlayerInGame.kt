package com.pdm.pokerdice.domain.game

data class PlayerInGame(
    val id: Int,
    val name: String,
    val currentBalance: Int,
    // val isActive: Boolean, // false se não conseguir pagar ante
    val moneyWon: Int,
)
