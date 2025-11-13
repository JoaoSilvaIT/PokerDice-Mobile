package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.repo.tm.Manager

class ManagerinMem : Manager{
    override val repoLobby = RepoLobbyInMem()
    override val repoUser = RepoUserInMem()
}