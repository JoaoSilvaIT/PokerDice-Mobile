package com.pdm.pokerdice.domain.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val MIN_ANTE = 10

@Parcelize
data class Round(
    // round number in the game
    val number: Int,
    val firstPlayerIdx: Int,
    val turn: Turn,
    val players: List<PlayerInGame>,
    val playerHands: Map<PlayerInGame, Hand>,
    val ante: Int = MIN_ANTE,
    val pot: Int = 0,
    val winners: List<PlayerInGame> = emptyList(),
    val foldedPlayers: List<PlayerInGame> = emptyList(),
    val gameId: Int,
) : Parcelable
