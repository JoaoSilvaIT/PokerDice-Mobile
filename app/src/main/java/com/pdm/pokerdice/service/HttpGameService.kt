package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.domain.utilis.success
import com.pdm.pokerdice.service.errors.GameError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.pdm.pokerdice.repository.http.model.DiceUpdateInputDto
import com.pdm.pokerdice.repository.http.model.GameDto
import com.pdm.pokerdice.repository.http.model.SetAnteInputDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class HttpGameService(
    private val client: HttpClient,
    private val authRepo: AuthInfoRepo
) : GameService {

    private val _currentGame = MutableStateFlow<Game?>(null)
    override val currentGame: Flow<Game?> = _currentGame.asStateFlow()

    private var pollingJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private suspend fun getToken(): String? = authRepo.getAuthInfo()?.authToken

    override suspend fun monitorGame(gameId: Int) {
        pollingJob?.cancel()
        pollingJob = scope.launch {
            while (true) {
                try {
                    val authInfo = authRepo.getAuthInfo()
                    if (authInfo != null) {
                        val gameDto = client.get("api/games/$gameId") {
                            header("Authorization", "Bearer ${authInfo.authToken}")
                        }.body<GameDto>()
                        val game = gameDto.toDomain(authInfo.userId)
                        _currentGame.value = game
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(1000) // Poll every 1 second for "real-time" feel
            }
        }
    }

    override suspend fun createGame(
        startedAt: Long,
        lobbyId: Int,
        numberOfRounds: Int,
        creatorId: Int
    ): Either<GameError, Game> {
        // HTTP API creates game implicitly via Lobby or separate endpoint? 
        // DAW API has POST /api/games with GameCreateInputModel.
        // For M5, assuming we create via Lobby/Start button which calls API.
        // We need to implement this in API if needed, but for now placeholder or use existing logic if adapted.
        return failure(GameError.GameNotFound) 
    }

    override suspend fun startGame(gameId: Int, creatorId: Int): Either<GameError, Game> {
        // DAW API: POST /api/games/{id}/start
        // Need to add this to PokerDiceApi if not there.
        return failure(GameError.GameNotFound)
    }

    override suspend fun rollDice(gameId: Int): Either<GameError, Unit> {
        return try {
            val token = getToken() ?: return failure(GameError.NetworkError("Not logged in"))
            client.post("api/games/$gameId/rounds/roll-dices") {
                header("Authorization", "Bearer $token")
            }
            success(Unit)
        } catch (e: Exception) {
            failure(GameError.NetworkError(e.message ?: "Unknown"))
        }
    }

    override suspend fun setAnte(gameId: Int, ante: Int): Either<GameError, Unit> {
        return try {
            val token = getToken() ?: return failure(GameError.NetworkError("Not logged in"))
            client.post("api/games/$gameId/rounds/ante") {
                header("Authorization", "Bearer $token")
                setBody(SetAnteInputDto(ante))
            }
            success(Unit)
        } catch (e: Exception) {
            failure(GameError.NetworkError(e.message ?: "Unknown"))
        }
    }

    override suspend fun nextTurn(gameId: Int, nextRoundAnte: Int?): Either<GameError, Unit> {
        return try {
            val token = getToken() ?: return failure(GameError.NetworkError("Not logged in"))
            client.post("api/games/$gameId/rounds/next-turn") {
                header("Authorization", "Bearer $token")
                setBody(SetAnteInputDto(nextRoundAnte))
            }
            success(Unit)
        } catch (e: Exception) {
            failure(GameError.NetworkError(e.message ?: "Unknown"))
        }
    }

    override suspend fun updateTurn(gameId: Int, diceChars: List<String>): Either<GameError, Unit> {
        return try {
            val token = getToken() ?: return failure(GameError.NetworkError("Not logged in"))
            client.post("api/games/$gameId/rounds/update-turn") {
                header("Authorization", "Bearer $token")
                setBody(DiceUpdateInputDto(diceChars))
            }
            success(Unit)
        } catch (e: Exception) {
            failure(GameError.NetworkError(e.message ?: "Unknown"))
        }
    }

    override suspend fun getLoggedUser(): UserExternalInfo? {
        val authInfo = authRepo.getAuthInfo() ?: return null
        return UserExternalInfo(authInfo.userId, "Me", 0) 
    }
}
