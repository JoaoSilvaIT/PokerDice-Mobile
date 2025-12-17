package com.pdm.pokerdice.repo.mem

import com.pdm.pokerdice.repo.RepositoryUser
import com.pdm.pokerdice.repo.tm.Transaction
import com.pdm.pokerdice.repo.tm.TransactionManager

class TransactionManagerInMem : TransactionManager {
    private val repoUsers = RepoUserInMem()
    private val repoLobby = RepoLobbyInMem()
    private val repoGame = RepoGameInMem()

    override fun <R> run(block: Transaction.() -> R): R {
        val transaction =
            TransactionInMem(
                repoUser = repoUsers,
                repoLobby = repoLobby,
                repoGame = repoGame
            )
        return transaction.block()
    }
}

class TransactionInMem(
    override val repoUser: RepoUserInMem,
    override val repoLobby: RepoLobbyInMem,
    override val repoGame: RepoGameInMem,
) : Transaction {
    override fun rollback() {
    }
}