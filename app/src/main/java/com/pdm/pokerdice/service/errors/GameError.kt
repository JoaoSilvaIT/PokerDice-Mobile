package com.pdm.pokerdice.service.errors

sealed                      class GameError {
    data object InvalidNumberOfRounds : GameError()

    data object InvalidTime : GameError()

    data object LobbyNotFound : GameError()

    data object LobbyHasActiveGame : GameError()

    data object UserNotLobbyHost : GameError()

    data object GameNotFound : GameError()

    data object GameAlreadyEnded : GameError()
}