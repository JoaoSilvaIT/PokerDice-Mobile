package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.utilis.State
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.domain.utilis.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.pdm.pokerdice.repo.tm.TransactionManager
import com.pdm.pokerdice.service.errors.GameError
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface GameService : Service {
    val games : Flow<List<Game>>

    fun createGame(startedAt: Long, lobbyId: Int, numberOfRounds: Int, creatorId: Int): Either<GameError, Game>

    fun startGame(gameId: Int, creatorId: Int): Either<GameError, Game>

    fun endGame(gameId: Int, endedAt: Long): Either<GameError, Game>
}

class FakeGameService(private val trxManager: TransactionManager, val repo : AuthInfoRepo) : GameService {
    private val _games = MutableStateFlow(emptyList<Game>())
    override val games: StateFlow<List<Game>> = _games.asStateFlow()

    override fun createGame(
        startedAt: Long,
        lobbyId: Int,
        numberOfRounds: Int,
        creatorId: Int,
    ): Either<GameError, Game> {
        if (numberOfRounds < 1) return failure(GameError.InvalidNumberOfRounds)
        if (startedAt <= 0) return failure(GameError.InvalidTime)
        return trxManager.run {
            val lobby = repoLobby.findById(lobbyId) ?: return@run failure(GameError.LobbyNotFound)

            val activeGames = repoGame.findActiveGamesByLobbyId(lobbyId)
            if (activeGames.isNotEmpty()) {
                return@run failure(GameError.LobbyHasActiveGame)
            }

            if (lobby.host.id != creatorId) return@run failure(GameError.UserNotLobbyHost)

            val game = repoGame.createGame(startedAt, lobby, numberOfRounds)
            success(game)
        }
    }

    override fun startGame(
        gameId: Int,
        creatorId: Int,
    ): Either<GameError, Game> {
        return trxManager.run {
            val game = repoGame.findById(gameId) ?: return@run failure(GameError.GameNotFound)
            val lobbyId = game.lobbyId ?: return@run failure(GameError.LobbyNotFound)
            val lobby = repoLobby.findById(lobbyId) ?: return@run failure(GameError.LobbyNotFound)

            val activeGames = repoGame.findActiveGamesByLobbyId(lobbyId)
            if (activeGames.any { it.id != gameId }) {
                return@run failure(GameError.LobbyHasActiveGame)
            }
            if (lobby.host.id != creatorId) return@run failure(GameError.UserNotLobbyHost)

            val newGame = game.copy(state = State.RUNNING)
            repoGame.save(newGame)

            success(newGame)
        }
    }

    override fun endGame(
        gameId: Int,
        endedAt: Long,
    ): Either<GameError, Game> =
        trxManager.run {
            val game = repoGame.findById(gameId) ?: return@run failure(GameError.GameNotFound)
            if (endedAt <= 0 || endedAt < game.startedAt) return@run failure(GameError.InvalidTime)
            if (game.endedAt != null) return@run failure(GameError.GameAlreadyEnded)
            val endedGame = game.copy(endedAt = endedAt, state = State.FINISHED)
            repoGame.save(endedGame)
            success(endedGame)
        }

    override suspend fun getLoggedUser(): UserExternalInfo? {
        val authInfo = repo.getAuthInfo() ?: return null

        return trxManager.run {
            repoUser.getUserById(authInfo.userId)
        }
    }
}