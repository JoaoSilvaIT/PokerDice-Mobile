package com.pdm.pokerdice.repo.tm

import com.pdm.pokerdice.repo.RepositoryLobby
import com.pdm.pokerdice.repo.RepositoryUser

interface Manager {
    val repoUser : RepositoryUser
    val repoLobby : RepositoryLobby
}