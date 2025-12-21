package com.pdm.pokerdice.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.utilis.HandRank
import com.pdm.pokerdice.domain.game.utilis.State
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.GameService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Represents the various visual states of the Game Screen.
 */
sealed class GameScreenState {
    object Loading : GameScreenState()

    data class SettingAnte(
        val game: Game,
        val isMyTurn: Boolean,
    ) : GameScreenState()

    data class Playing(
        val game: Game,
        val myUserId: Int,
        val isMyTurn: Boolean,
        val currentRollCount: Int,
        val heldDiceIndexes: Set<Int>, // Indices into rolledDice
        val rolledDice: List<Dice> = emptyList(), // Ephemeral dice from last roll
        val currentHandRank: HandRank? = null,
        val isRolling: Boolean = false,
        val opponentDice: List<Dice> = emptyList(),
        val opponentHandRank: HandRank? = null,
    ) : GameScreenState()

    object RoundEndedStub : GameScreenState() // Keep for type compatibility if needed

    data class MatchEnded(
        val winnerName: String,
        val finalScore: Int,
    ) : GameScreenState()

    data class Error(
        val message: String,
    ) : GameScreenState()
}

class GameViewModel(
    private val service: GameService,
    private val authRepo: AuthInfoRepo,
) : ViewModel() {
    var screenState by mutableStateOf<GameScreenState>(GameScreenState.Loading)
        private set
    var lastWinnerNotification by mutableStateOf<String?>(null)
        private set
    private var currentGame: Game? = null
    private var myUserId: Int = -1
    private var monitoringJob: Job? = null
    private var isTransactionInProgress = false
    private var lastObservedRoundNumber = 0

    fun initialize(gameId: Int) {
        viewModelScope.launch {
            val authInfo = authRepo.getAuthInfo()
            if (authInfo == null) {
                screenState = GameScreenState.Error("User not logged in")
                return@launch
            }
            myUserId = authInfo.userId

            startMonitoring(gameId)
        }
    }

    private fun startMonitoring(gameId: Int) {
        monitoringJob?.cancel()
        monitoringJob =
            viewModelScope.launch {
                service.monitorGame(gameId)
                service.currentGame.collect { game ->
                    if (game != null && !isTransactionInProgress) {
                        // Detect winner of previous round before updating currentGame
                        detectRoundWinner(game)

                        currentGame = game
                        updateScreenState(game)
                    }
                }
            }
    }

    private fun detectRoundWinner(newGame: Game) {
        val newRound = newGame.currentRound ?: return
        val currentRoundNum = newRound.number

        if (lastObservedRoundNumber in 1..<currentRoundNum) {
            val previousPlayers = currentGame?.players ?: emptyList()
            val winners =
                newGame.players.filter { p ->
                    val prev = previousPlayers.find { it.id == p.id }
                    p.moneyWon > (prev?.moneyWon ?: 0)
                }

            if (winners.isNotEmpty()) {
                lastWinnerNotification = "Round $lastObservedRoundNumber Winner: ${winners.joinToString { it.name }}"
                viewModelScope.launch {
                    delay(5000)
                    lastWinnerNotification = null
                }
            }
        }
        lastObservedRoundNumber = currentRoundNum
    }

    fun startGame(game: Game) {
        viewModelScope.launch {
            isTransactionInProgress = true
            screenState = GameScreenState.Loading
            val result =
                service.startGame(
                    gameId = game.id,
                    creatorId = myUserId,
                )

            when (result) {
                is Either.Success -> {
                    currentGame = result.value
                    nextRound()
                    updateScreenState(result.value)
                }
                is Either.Failure -> {
                    screenState = GameScreenState.Error("Failed to start game: ${result.value}")
                }
            }
            isTransactionInProgress = false
        }
    }

    private fun updateScreenState(game: Game) {
        if (game.state == State.FINISHED) {
            val winner = game.players.maxByOrNull { it.moneyWon }
            screenState =
                GameScreenState.MatchEnded(
                    winnerName = winner?.name ?: "Draw",
                    finalScore = winner?.moneyWon ?: 0,
                )
            return
        }

        val round =
            game.currentRound ?: run {
                screenState = GameScreenState.Loading
                return
            }

        // Removed explicit RoundEnded state check as backend auto-advances

        // Logic for Ante vs Playing
        if (round.ante == 0) {
            val isMyTurn = round.turn.player.id == myUserId
            screenState = GameScreenState.SettingAnte(game, isMyTurn)
            return
        }

        // Playing State
        val isMyTurn = round.turn.player.id == myUserId

        // Reset held dice if it's no longer my turn or a new round/turn started
        val currentPlayingState = screenState as? GameScreenState.Playing
        val lastTurnPlayerId =
            currentPlayingState
                ?.game
                ?.currentRound
                ?.turn
                ?.player
                ?.id

        val sameTurn =
            currentPlayingState != null &&
                lastTurnPlayerId == round.turn.player.id &&
                currentPlayingState.game.currentRound?.number == round.number

        val rolledDice = if (sameTurn) currentPlayingState!!.rolledDice else emptyList()
        val heldDice = if (sameTurn) currentPlayingState!!.heldDiceIndexes else emptySet()

        val lockedDice = round.turn.currentDice
        // Calculate Rank: Locked Dice + Held Ephemeral Dice ONLY
        val activeDice = heldDice.mapNotNull { if (it < rolledDice.size) rolledDice[it] else null }

        val totalHandForRank = lockedDice + activeDice
        val rank = if (totalHandForRank.isNotEmpty()) calculatePartialRank(totalHandForRank) else null

        screenState =
            GameScreenState.Playing(
                game = game,
                myUserId = myUserId,
                isMyTurn = isMyTurn,
                currentRollCount = 3 - round.turn.rollsRemaining,
                heldDiceIndexes = heldDice,
                rolledDice = rolledDice,
                currentHandRank = rank,
                isRolling = false,
            )
    }

    fun submitAnte(amount: Int) {
        val game = currentGame ?: return
        viewModelScope.launch {
            val result = service.setAnte(game.id, amount)
            if (result is Either.Success) {
                service.payAnte(game.id)
            }
        }
    }

    fun holdDice() {
        val game = currentGame ?: return
        val state = screenState as? GameScreenState.Playing ?: return
        if (!state.isMyTurn || state.heldDiceIndexes.isEmpty()) return

        viewModelScope.launch {
            isTransactionInProgress = true

            // 1. Get Selected Dice
            val heldIndices = state.heldDiceIndexes
            val heldDiceList = state.rolledDice.filterIndexed { index, _ -> index in heldIndices }
            val heldFacesStr = heldDiceList.map { faceToCharString(it.face) }

            // 2. Send to Backend
            val result = service.updateTurn(game.id, heldFacesStr)

            if (result is Either.Success) {
                // 3. Update Local Game State (Lock the dice)
                val currentRound = game.currentRound!!
                val newLocked = currentRound.turn.currentDice + heldDiceList
                val newTurn = currentRound.turn.copy(currentDice = newLocked)
                val newRound = currentRound.copy(turn = newTurn)
                val newGame = game.copy(currentRound = newRound)

                currentGame = newGame

                // 4. Update Ephemeral State (Remove locked from rolled, clear selection)
                val remainingRolled = state.rolledDice.filterIndexed { index, _ -> index !in heldIndices }

                // 5. Apply State Update
                // We manually update to ensure instant transition without waiting for poll
                screenState =
                    state.copy(
                        game = newGame,
                        rolledDice = remainingRolled,
                        heldDiceIndexes = emptySet(),
                    )
            }
            isTransactionInProgress = false
        }
    }

    fun rollDice() {
        val game = currentGame ?: return
        val state = screenState as? GameScreenState.Playing ?: return
        if (!state.isMyTurn) return

        viewModelScope.launch {
            isTransactionInProgress = true
            screenState = state.copy(isRolling = true)

            // Just Roll. heldDiceIndexes are ignored (must hold explicitly first).
            val result = service.rollDice(game.id)

            delay(500)

            if (result is Either.Success) {
                val newDice =
                    result.value.map { faceStr ->
                        Dice(parseFace(faceStr))
                    }

                // Manually decrement rolls in local state for instant update
                val currentRound = game.currentRound!!
                val newTurn = currentRound.turn.copy(rollsRemaining = currentRound.turn.rollsRemaining - 1)
                val newRound = currentRound.copy(turn = newTurn)
                val newGame = game.copy(currentRound = newRound)
                currentGame = newGame

                val newState = screenState as? GameScreenState.Playing
                if (newState != null) {
                    screenState =
                        newState.copy(
                            game = newGame,
                            rolledDice = newDice,
                            currentRollCount = 3 - newTurn.rollsRemaining,
                            isRolling = false,
                        )
                }
            } else {
                val newState = screenState as? GameScreenState.Playing
                if (newState != null) {
                    screenState = newState.copy(isRolling = false)
                }
            }
            isTransactionInProgress = false
        }
    }

    fun toggleHoldDie(index: Int) {
        val currentState = screenState as? GameScreenState.Playing ?: return
        if (!currentState.isMyTurn) return

        val currentHolds = currentState.heldDiceIndexes.toMutableSet()
        if (currentHolds.contains(index)) {
            currentHolds.remove(index)
        } else {
            currentHolds.add(index)
        }

        screenState = currentState.copy(heldDiceIndexes = currentHolds)
    }

    fun endTurn() {
        val game = currentGame ?: return
        val state = screenState as? GameScreenState.Playing ?: return

        viewModelScope.launch {
            // Lock ALL remaining dice
            val allRolledFaces = state.rolledDice.map { faceToCharString(it.face) }
            if (allRolledFaces.isNotEmpty()) {
                service.updateTurn(game.id, allRolledFaces)
            }

            service.nextTurn(game.id, null)
            screenState = state.copy(rolledDice = emptyList(), heldDiceIndexes = emptySet())
        }
    }

    fun nextRound() {
        val game = currentGame ?: return
        viewModelScope.launch {
            service.startNewRound(game.id, null)
        }
    }

    companion object {
        fun getFactory(
            service: GameService,
            authRepo: AuthInfoRepo,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { GameViewModel(service, authRepo) }
            }
    }
}
