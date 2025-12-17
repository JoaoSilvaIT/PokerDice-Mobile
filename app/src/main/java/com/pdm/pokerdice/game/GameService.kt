package com.pdm.pokerdice.game

import com.pdm.pokerdice.domain.AuthInfoRepo
import com.pdm.pokerdice.domain.game.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.pdm.pokerdice.repo.tm.Transaction
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface GameService {
    val games : Flow<List<Game>>

    fun createGame(startedAt: Long, lobbyId: Int, numberOfRounds: Int, creatorId: Int)
}

class FakeGameService(val manager: Transaction, val repo : AuthInfoRepo) : GameService {
    private val _games = MutableStateFlow(manager.repoGame.findAll())
    override val games: StateFlow<List<Game>> = _games.asStateFlow()

    override fun createGame(startedAt: Long, lobbyId: Int, numberOfRounds: Int, creatorId: Int) {
        TODO("Not yet implemented")
    }
}