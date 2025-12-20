package com.pdm.pokerdice.repository.http.model

import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.PlayerInGame
import com.pdm.pokerdice.domain.game.Round
import com.pdm.pokerdice.domain.game.Turn
import com.pdm.pokerdice.domain.game.utilis.Face
import com.pdm.pokerdice.domain.game.utilis.State
import com.pdm.pokerdice.domain.lobby.Lobby
import com.pdm.pokerdice.domain.lobby.LobbySettings
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
            settings = LobbySettings(
                numberOfRounds = 5, // Default as not present in DTO
                minPlayers = minPlayers,
                maxPlayers = maxPlayers
            ),
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
        val domainPlayers = safePlayers.map { it.toDomain() }
        
        // Map current round if exists
        val domainRound = currentRound?.toDomain(domainPlayers)

        return Game(
            id = id,
            lobbyId = lobbyId,
            players = domainPlayers,
            numberOfRounds = numberOfRounds,
            state = gameState,
            currentRound = domainRound,
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
    fun toDomain(gamePlayers: List<PlayerInGame>): Round {
        // Map dice strings to Dice domain
        val diceList = (currentDice ?: emptyList()).filterNotNull().map { diceStr -> 
            try {
                // Try to parse by Face name first (e.g. "ACE")
                Dice(Face.valueOf(diceStr.uppercase()))
            } catch (e: Exception) {
                // Try single char mapping
                val face = when(diceStr.uppercase().firstOrNull()) {
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
        val turnPlayer = gamePlayers.find { it.id == turnUserId }
            ?: PlayerInGame(turnUserId, "Unknown", 0, 0)

        val turn = Turn(
            player = turnPlayer,
            rollsRemaining = rollsLeft,
            currentDice = diceList,
            isFolded = false // Default
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
    fun toDomain() = PlayerInGame(id, name ?: "Unknown", currentBalance, moneyWon, handRank)
    
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

data class RolledDiceOutputDto(
    val dice: List<String>
)
