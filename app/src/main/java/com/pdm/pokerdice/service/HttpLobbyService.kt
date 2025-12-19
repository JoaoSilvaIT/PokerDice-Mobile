package com.pdm.pokerdice.service

import io.ktor.client.HttpClient
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.domain.utilis.success
import com.pdm.pokerdice.repository.http.model.LobbyDto
import com.pdm.pokerdice.repository.http.model.LobbiesListDto
import com.pdm.pokerdice.service.errors.LobbyError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class HttpLobbyService(
    private val client: HttpClient,
    private val authRepo: AuthInfoRepo
) : LobbyService {

    private suspend fun getToken(): String? = authRepo.getAuthInfo()?.authToken

    // Real-time via Polling for M5 (2 seconds)
    override val lobbies: Flow<List<Lobby>> = flow {
        while (true) {
            try {
                val token = getToken()
                if (token != null) {
                    val response = client.get("api/lobbies") {
                        header("Authorization", "Bearer $token")
                    }.body<LobbiesListDto>()
                    
                    val list = (response.lobbies ?: emptyList()).filterNotNull().mapNotNull {
                        try {
                            it.toDomain() 
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                    }
                    emit(list)
                } else {
                    emit(emptyList()) 
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Emit empty list on error so UI stops loading
                emit(emptyList())
            }
            delay(2000)
        }
    }

    override fun refreshLobbies() {
        // Polling handles this automatically
    }

    override suspend fun createLobby(
        name: String,
        description: String,
        minPlayers: Int,
        maxPlayers: Int,
        host: UserExternalInfo
    ): Either<LobbyError, Lobby> {
        return try {
            val token = getToken() ?: return failure(LobbyError.NetworkError("Not logged in"))
            val response = client.post("api/lobbies") {
                header("Authorization", "Bearer $token")
                setBody(mapOf(
                    "name" to name,
                    "description" to description,
                    "minPlayers" to minPlayers,
                    "maxPlayers" to maxPlayers
                ))
            }
            
            if (response.status == HttpStatusCode.Unauthorized) {
                return failure(LobbyError.NetworkError("Session expired. Please login again."))
            }
            
            if (response.status != HttpStatusCode.Created && response.status != HttpStatusCode.OK) {
                return failure(LobbyError.NetworkError("Server error: ${response.status}"))
            }

            val lobbyDto = response.body<LobbyDto>()
            success(lobbyDto.toDomain())
        } catch (e: Exception) {
            failure(LobbyError.NetworkError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun joinLobby(usr: UserExternalInfo, lobbyId: Int): Either<LobbyError, Lobby> {
        return try {
            val token = getToken() ?: return failure(LobbyError.NetworkError("Not logged in"))
            val lobbyDto = client.post("api/lobbies/$lobbyId/join") {
                header("Authorization", "Bearer $token")
            }.body<LobbyDto>()
            success(lobbyDto.toDomain())
        } catch (e: Exception) {
            failure(LobbyError.NetworkError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun leaveLobby(usr: UserExternalInfo, lobbyId: Int): Either<LobbyError, Boolean> {
        return try {
            val token = getToken() ?: return failure(LobbyError.NetworkError("Not logged in"))
            client.post("api/lobbies/$lobbyId/leave") {
                header("Authorization", "Bearer $token")
            }
            success(true)
        } catch (e: Exception) {
            failure(LobbyError.NetworkError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun getLoggedUser(): UserExternalInfo? {
        val authInfo = authRepo.getAuthInfo() ?: return null
        return UserExternalInfo(authInfo.userId, "Me", 0)
    }
}
