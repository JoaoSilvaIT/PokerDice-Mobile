package com.pdm.pokerdice.domain.game

import com.pdm.pokerdice.domain.game.utilis.Face

data class GameRoundDto(
    val number: Int,
    val ante: Int,
    val turnUserId: Int,
    val rollsLeft: Int,
    val currentDice: List<String?>?,
    val pot: Int,
    val winners: List<PlayerInGameDto?>?,
    val players: List<PlayerInGameDto?>?,
) {
    fun toDomain(gamePlayers: List<PlayerInGame>): Round {
        // Map dice strings to Dice domain
        val diceList =
            (currentDice ?: emptyList()).filterNotNull().map { diceStr ->
                try {
                    // Try to parse by Face name first (e.g. "ACE")
                    Dice(Face.valueOf(diceStr.uppercase()))
                } catch (e: Exception) {
                    // Try single char mapping
                    val face =
                        when (diceStr.uppercase().firstOrNull()) {
                            'A' -> Face.ACE
                            'K' -> Face.KING
                            'Q' -> Face.QUEEN
                            'J' -> Face.JACK
                            'T' -> Face.TEN
                            '9' -> Face.NINE
                            else -> Face.ACE // Ultimate Fallback
                        }
                    Dice(face)
                }
            }

        // Find current player in game players
        val turnPlayer =
            gamePlayers.find { it.id == turnUserId }
                ?: PlayerInGame(turnUserId, "Unknown", 0, 0)

        val turn =
            Turn(
                player = turnPlayer,
                rollsRemaining = rollsLeft,
                currentDice = diceList,
                isFolded = false, // Default
            )

        val safeWinners = winners?.filterNotNull()?.map { it.toDomain() } ?: emptyList()

        return Round(
            number = number,
            firstPlayerIdx = 0, // Not provided in DTO
            turn = turn,
            players = gamePlayers,
            playerHands = emptyMap(), // Not in DTO
            ante = ante,
            pot = pot,
            winners = safeWinners,
            foldedPlayers = emptyList(), // Not in DTO
            gameId = 0,
        )
    }
}
