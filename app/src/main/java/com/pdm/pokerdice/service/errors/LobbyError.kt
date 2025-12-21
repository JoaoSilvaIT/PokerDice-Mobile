package com.pdm.pokerdice.service.errors

sealed class LobbyError {
    data object BlankName : LobbyError()

    data object MinPlayersTooLow : LobbyError()

    data object MaxPlayersTooHigh : LobbyError()

    data object MinPlayersGreaterThanMax : LobbyError()

    data object NameAlreadyUsed : LobbyError()

    data object LobbyNotFound : LobbyError()

    data object LobbyFull : LobbyError()

    data object UserAlreadyInLobby : LobbyError()

    data object UserNotInLobby : LobbyError()

    data class NetworkError(
        val message: String,
    ) : LobbyError()
}
