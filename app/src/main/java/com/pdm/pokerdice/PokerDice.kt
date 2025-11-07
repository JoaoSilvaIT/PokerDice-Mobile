package com.pdm.pokerdice

import android.app.Application
import com.pdm.pokerdice.repo.RepositoryLobby
import com.pdm.pokerdice.repo.mem.RepoLobbyInMem
import com.pdm.pokerdice.service.FakeLobbyService
import com.pdm.pokerdice.service.LobbyService

interface DependenciesContainer {
    val lobbyService: LobbyService
}

class DemosHostApplication : DependenciesContainer, Application() {
    override val lobbyService: LobbyService by lazy { FakeLobbyService(RepoLobbyInMem()) }
}
