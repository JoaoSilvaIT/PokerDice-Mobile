package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.Hand
import com.pdm.pokerdice.domain.game.PlayerInGame
import com.pdm.pokerdice.domain.game.Round
import com.pdm.pokerdice.domain.game.utilis.State
import com.pdm.pokerdice.repo.RepositoryGame

class RepoGameInMem : RepositoryGame{
    private val games = mutableListOf<Game>()
    private var nextId = 1

    override fun createGame(
        startedAt: Long,
        lobby: Lobby,
        numberOfRounds: Int
    ): Game {
        val players = lobby.players.map { 
            PlayerInGame(it.id, it.name, it.balance, 0)
        }
        val game = Game(
            id = nextId++,
            lobbyId = lobby.id,
            players = players,
            numberOfRounds = numberOfRounds,
            state = State.WAITING, // or WAITING? Assuming RUNNING for now based on immediate creation
            currentRound = null, // Or create the first round?
            startedAt = startedAt,
            endedAt = null
        )
        games.add(game)
        return game
    }

    override fun findActiveGamesByLobbyId(lobbyId: Int): List<Game> {
        return games.filter { it.lobbyId == lobbyId && it.state != State.FINISHED && it.state != State.TERMINATED }
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
        games.addAll(entities)
    }

    override fun findById(id: Int): Game? {
        return games.find { it.id == id }
    }

    override fun findAll(): List<Game> {
        return games.toList()
    }

    override fun save(entity: Game) {
        val index = games.indexOfFirst { it.id == entity.id }
        if (index != -1) {
            games[index] = entity
        } else {
            games.add(entity)
        }
    }

    override fun deleteById(id: Int) {
        games.removeIf { it.id == id }
    }

    override fun clear() {
        games.clear()
    }
}