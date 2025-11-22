package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.game.Game
import kotlinx.coroutines.flow.Flow

interface GameService {
    val games : Flow<Game>
}