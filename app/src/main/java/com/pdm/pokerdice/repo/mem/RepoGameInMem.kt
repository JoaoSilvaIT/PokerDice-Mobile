package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.Lobby
import com.pdm.pokerdice.domain.User
import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.Hand
import com.pdm.pokerdice.domain.game.Round
import com.pdm.pokerdice.repo.RepositoryGame

class RepoGameInMem : RepositoryGame{
    override fun createGame(
        startedAt: Long,
        lobby: Lobby,
        numberOfRounds: Int
    ): Game {
        TODO("Not yet implemented")
    }

    override fun endGame(
        game: Game,
        endedAt: Long
    ): Game {
        TODO("Not yet implemented")
    }

    override fun updateGameRound(
        round: Round,
        game: Game
    ): Game {
        TODO("Not yet implemented")
    }

    override fun startNewRound(game: Game): Game {
        TODO("Not yet implemented")
    }

    override fun setAnte(
        ante: Int,
        round: Round
    ): Round {
        TODO("Not yet implemented")
    }

    override fun payAnte(round: Round): Round {
        TODO("Not yet implemented")
    }

    override fun distributeWinnings(round: Round): Round {
        TODO("Not yet implemented")
    }

    override fun nextTurn(round: Round): Round {
        TODO("Not yet implemented")
    }

    override fun updateTurn(
        chosenDice: Dice,
        round: Round
    ): Round {
        TODO("Not yet implemented")
    }

    override fun loadPlayerHands(
        gameId: Int,
        roundNumber: Int,
        players: List<User>
    ): Map<User, Hand> {
        TODO("Not yet implemented")
    }

    override fun addAll(entities: List<Game>) {
        TODO("Not yet implemented")
    }

    override fun findById(id: Int): Game? {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Game> {
        TODO("Not yet implemented")
    }

    override fun save(entity: Game) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}