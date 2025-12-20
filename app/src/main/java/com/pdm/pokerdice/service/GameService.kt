package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.errors.GameError
import kotlinx.coroutines.flow.Flow

interface GameService : Service {
    /**
     * Emits the current state of the monitored game.
     */
    val currentGame: Flow<Game?>

    /**
     * Starts monitoring a specific game (e.g., via polling or SSE).
     */
    suspend fun monitorGame(gameId: Int)

    suspend fun createGame(startedAt: Long, lobbyId: Int, numberOfRounds: Int, creatorId: Int): Either<GameError, Game>
    suspend fun startGame(gameId: Int, creatorId: Int): Either<GameError, Game>
    
    // Game Actions
    suspend fun rollDice(gameId: Int): Either<GameError, List<String>>
    suspend fun setAnte(gameId: Int, ante: Int): Either<GameError, Unit>
    suspend fun payAnte(gameId: Int): Either<GameError, Unit>
    suspend fun startNewRound(gameId: Int, ante: Int?): Either<GameError, Unit>
    suspend fun nextTurn(gameId: Int, nextRoundAnte: Int?): Either<GameError, Unit>
    suspend fun updateTurn(gameId: Int, diceChars: List<String>): Either<GameError, Unit>
}
