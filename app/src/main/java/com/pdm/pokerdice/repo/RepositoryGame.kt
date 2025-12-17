package com.pdm.pokerdice.repo

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.Hand
import com.pdm.pokerdice.domain.game.Round

interface RepositoryGame : Repository<Game> {
    fun createGame(
        startedAt: Long,
        lobby: Lobby,
        numberOfRounds: Int,
    ): Game

    fun endGame(
        game: Game,
        endedAt: Long,
    ): Game

    fun updateGameRound(
        round: Round,
        game: Game,
    ): Game

    fun startNewRound(game: Game): Game

    fun setAnte(
        ante: Int,
        round: Round,
    ): Round

    fun payAnte(round: Round): Round

    fun distributeWinnings(round: Round): Round

    fun nextTurn(round: Round): Round

    fun updateTurn(
        chosenDice: Dice,
        round: Round,
    ): Round

    fun loadPlayerHands(
        gameId: Int,
        roundNumber: Int,
        players: List<User>,
    ): Map<User, Hand>

    fun findActiveGamesByLobbyId(lobbyId: Int): List<Game>
}