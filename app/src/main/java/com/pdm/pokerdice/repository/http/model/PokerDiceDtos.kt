package com.pdm.pokerdice.repository.http.model

import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.PlayerInGame
import com.pdm.pokerdice.domain.game.Round
import com.pdm.pokerdice.domain.game.Turn
import com.pdm.pokerdice.domain.game.utilis.Face
import com.pdm.pokerdice.domain.game.utilis.State
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.user.UserStatistics

data class LobbiesListDto(
    val lobbies: List<LobbyDto?>?
)

data class LobbyDto(
    val id: Int,
    val name: String?,
    val description: String?,
    val minPlayers: Int,
    val maxPlayers: Int,
    val players: List<LobbyPlayerDto?>?,
    val hostId: Int
) {
    fun toDomain(): Lobby {
        val safePlayers = players?.filterNotNull() ?: emptyList()
        val host = safePlayers.find { it.id == hostId } ?: LobbyPlayerDto(hostId, "Unknown")
        return Lobby(
            id = id,
            name = name ?: "Unknown Lobby",
            description = description ?: "",
            minPlayers = minPlayers,
            maxPlayers = maxPlayers,
            players = safePlayers.map { it.toDomain() }.toSet(),
            host = host.toDomain()
        )
    }
}

data class LobbyPlayerDto(
    val id: Int,
    val name: String?
) {
    fun toDomain() = UserExternalInfo(id, name ?: "Unknown", 0) // Balance not in LobbyPlayerDto?
}

data class GameDto(
    val id: Int,
    val startedAt: Long,
    val endedAt: Long?,
    val lobbyId: Int?,
    val numberOfRounds: Int,
    val state: String?,
    val currentRound: GameRoundDto?,
    val players: List<PlayerInGameDto?>?
) {
    fun toDomain(currentUserId: Int): Game {
        val gameState = try { State.valueOf(state ?: "RUNNING") } catch (e: Exception) { State.RUNNING }
        val safePlayers = players?.filterNotNull() ?: emptyList()
        return Game(
            id = id,
            lobbyId = lobbyId ?: 0,
            players = safePlayers.map { it.toDomain() },
            numberOfRounds = numberOfRounds,
            state = gameState,
            currentRound = currentRound?.toDomain(safePlayers),
            startedAt = startedAt,
            endedAt = endedAt
        )
    }
}

data class GameRoundDto(
    val number: Int,
    val ante: Int,
    val turnUserId: Int,
    val rollsLeft: Int,
    val currentDice: List<String?>?,
    val pot: Int,
    val winners: List<PlayerInGameDto?>?,
    val players: List<PlayerInGameDto?>?
) {
    fun toDomain(gamePlayers: List<PlayerInGameDto>): Round {
        // Map dice strings to Dice domain
        val diceList = (currentDice ?: emptyList()).filterNotNull().map { diceStr -> 
            // Expecting something like "ACE", "KING" or "1", "6"?
            // Assuming string representation of Face enum
            try {
                Dice(Face.valueOf(diceStr.uppercase()))
            } catch (e: Exception) {
                Dice(Face.ACE) // Fallback
            }
        }

        val safePlayers = players?.filterNotNull() ?: gamePlayers
        
        // Find current player
        val turnPlayer = safePlayers.find { it.id == turnUserId }?.toDomainUser() 
            ?: User(turnUserId, "Unknown", "", "", 0, UserStatistics(0,0,0,0.0))

        val turn = Turn(
            player = turnPlayer,
            rollsRemaining = rollsLeft,
            currentDice = diceList
        )

        return Round(
            number = number,
            firstPlayerIdx = 0, // Not provided in DTO?
            turn = turn,
            players = safePlayers.map { it.toDomain() },
            playerHands = emptyMap(), // Not in DTO?
            ante = ante,
            pot = pot,
            winners = winners?.filterNotNull()?.map { it.toDomain() } ?: emptyList(),
            gameId = 0
        )
    }
}

data class PlayerInGameDto(
    val id: Int,
    val name: String?,
    val currentBalance: Int,
    val moneyWon: Int,
    val handRank: String?
) {
    fun toDomain() = PlayerInGame(id, name ?: "Unknown", currentBalance, moneyWon)
    
    fun toDomainUser() = User(id, name ?: "Unknown", "", "", currentBalance, UserStatistics(0,0,0,0.0))
}

data class SetAnteInputDto(
    val ante: Int?
)

data class DiceUpdateInputDto(
    val dices: List<String>
)

data class LoginInputDto(
    val email: String,
    val password: String
)

data class LoginOutputDto(
    val token: String
)

data class SignUpInputDto(
    val username: String,
    val email: String,
    val password: String,
    val invitationCode: String
)

data class UserOutputDto(
    val name: String,
    val balance: Int
)

data class MeOutputDto(
    val id: Int,
    val name: String,
    val email: String,
    val balance: Int
) {
    fun toDomain(stats: UserStatistics) = User(id, name, email, "", balance, stats)
}

data class UserStatisticsDto(
    val gamesPlayed: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Double
) {
    fun toDomain() = UserStatistics(gamesPlayed, wins, losses, winRate)
}
