package com.pdm.pokerdice.repo.tm

import com.pdm.pokerdice.repo.RepositoryGame
import com.pdm.pokerdice.repo.RepositoryLobby
import com.pdm.pokerdice.repo.RepositoryUser

interface Transaction {
    val repoUser: RepositoryUser
    val repoLobby: RepositoryLobby
    val repoGame: RepositoryGame

    fun rollback()
}
