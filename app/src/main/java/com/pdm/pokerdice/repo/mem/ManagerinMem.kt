package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.repo.tm.Transaction

class ManagerinMem : Transaction{
    override val repoLobby = RepoLobbyInMem()
    override val repoUser = RepoUserInMem()
}