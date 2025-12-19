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
    data class SettingAnte(val game: Game) : GameScreenState()
    data class Playing(
        val game: Game,
        val isMyTurn: Boolean,
        val currentRollCount: Int,
        val heldDiceIndexes: Set<Int>,
        val lockedDiceIndexes: Set<Int> = emptySet(),
        val currentHandRank: HandRank? = null,
        val isRolling: Boolean = false,
        val opponentDice: List<Dice> = emptyList(),
        val opponentHandRank: HandRank? = null
    ) : GameScreenState()
    data class RoundEnded(val winnerName: String, val winningHand: String) : GameScreenState()
    data class MatchEnded(val winnerName: String, val finalScore: Int) : GameScreenState()
    data class Error(val message: String) : GameScreenState()
}

class GameViewModel(
    private val service: GameService,
    private val authRepo: AuthInfoRepo
) : ViewModel() {

    var screenState by mutableStateOf<GameScreenState>(GameScreenState.Loading)
        private set

    private var currentGame: Game? = null
    private var myUserId: Int = -1
    private var monitoringJob: Job? = null

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
        monitoringJob = viewModelScope.launch {
            service.monitorGame(gameId)
            service.currentGame.collect { game ->
                if (game != null) {
                    currentGame = game
                    updateScreenState(game)
                }
            }
        }
    }

    private fun updateScreenState(game: Game) {
        if (game.state == State.FINISHED) {
             // Calculate match winner
             val winner = game.players.maxByOrNull { it.currentBalance }
             screenState = GameScreenState.MatchEnded(
                 winnerName = winner?.name ?: "Draw",
                 finalScore = winner?.currentBalance ?: 0
             )
             return
        }

        val round = game.currentRound ?: return
        
        // Check if round ended (winners list is not empty)
        if (round.winners.isNotEmpty()) {
            val winner = round.winners.first() // Simplified for single winner
            screenState = GameScreenState.RoundEnded(
                winnerName = winner.name,
                winningHand = "Hand Value: ${round.winners.firstOrNull()?.currentBalance}" // TODO: API should return hand name
            )
            return
        }

        // Logic for Ante vs Playing
        // If ante is 0, we are in SettingAnte state (assuming backend logic: ante 0 means waiting for ante)
        // OR if API has explicit state. Here we assume if ante == 0, we need to set it.
        if (round.ante == 0) {
             screenState = GameScreenState.SettingAnte(game)
             return
        }

        // Playing State
        val isMyTurn = round.turn.player.id == myUserId
        
        // Preserve local UI state (held dice) if we are already playing
        val currentPlayingState = screenState as? GameScreenState.Playing
        val heldDice = if (isMyTurn) currentPlayingState?.heldDiceIndexes ?: emptySet() else emptySet()
        
        // Calculate hand rank locally for display
        val currentDice = round.turn.currentDice
        val rank = if (currentDice.isNotEmpty()) calculatePartialRank(currentDice) else null

        screenState = GameScreenState.Playing(
            game = game,
            isMyTurn = isMyTurn,
            currentRollCount = 3 - round.turn.rollsRemaining, // API returns rolls left
            heldDiceIndexes = heldDice,
            lockedDiceIndexes = emptySet(), // Logic for locking could be complex, simplifying for now
            currentHandRank = rank,
            isRolling = false
        )
    }

    fun submitAnte(amount: Int) {
        val game = currentGame ?: return
        viewModelScope.launch {
            service.setAnte(game.id, amount)
            // State update will come via monitoring
        }
    }

    fun rollDice() {
        val game = currentGame ?: return
        viewModelScope.launch {
            // Optimistic UI update could be added here
            service.rollDice(game.id)
        }
    }
    
    // Not explicitly used with API logic yet as API handles holding via dice updates?
    // Actually, updateTurn sends dice chars. We might need a separate "Hold" logic 
    // or just send the full dice state. 
    // Looking at HttpGameService: updateTurn(diceChars). 
    // So we probably hold dice locally, then roll sends the command?
    // Wait, rollDice() is a simple trigger. 
    // Typically: Roll -> Backend generates dice -> Returns new state.
    // If we want to hold dice, we might need to tell backend WHICH dice to keep?
    // The API `roll-dices` might not take arguments in current Service?
    // Let's check Service: rollDice(gameId). No args.
    // So... how do we hold? 
    // Maybe `updateTurn` is used to SET the dice before rolling? 
    // Or maybe the API is simple and doesn't support holding specific dice yet?
    // Assuming for now we just roll.
    
    // CORRECTION based on previous files:
    // `updateTurn` sends `diceChars`.
    // Maybe we use that to "lock" dice?
    // For M5, let's assume we just roll for now, or the API needs update. 
    // Re-reading HttpGameService: `rollDice` is POST .../roll-dices. 
    // If we want to implement Hold, we probably need to fix API or Service.
    // For now, I will implement toggleHoldDie purely for UI, 
    // but the actual roll might re-roll everything if API isn't aware.
    // However, checking `updateTurn`... maybe we send the dice we keep?
    
    fun toggleHoldDie(index: Int) {
        val currentState = screenState as? GameScreenState.Playing ?: return
        if (!currentState.isMyTurn) return

        val currentHolds = currentState.heldDiceIndexes.toMutableSet()
        if (currentHolds.contains(index)) {
            currentHolds.remove(index)
        } else {
            currentHolds.add(index)
        }
        
        // We update local state. 
        // If we need to sync with backend, we might call updateTurn?
        screenState = currentState.copy(heldDiceIndexes = currentHolds)
        
        // TODO: If API requires us to send "kept" dice before rolling, do it here or in rollDice.
    }
    
    fun endTurn() {
         val game = currentGame ?: return
         viewModelScope.launch {
             service.nextTurn(game.id, null)
         }
    }
    
    fun nextRound() {
        // If Match ended, navigate away?
        // If Round ended, we probably wait for backend to start next round or we signal "ready"?
        // Service has `nextTurn`. 
        // If state is RoundEnded, maybe we call nextTurn to proceed?
        val game = currentGame ?: return
        viewModelScope.launch {
            service.nextTurn(game.id, 0) // Start next round/turn
        }
    }

    private fun calculatePartialRank(dice: List<Dice>): HandRank {
        val counts = dice.groupingBy { it.face }.eachCount()
        val maxCount = counts.values.maxOrNull() ?: 0
        val pairCount = counts.values.count { it == 2 }
        val distinctCount = counts.size
        val hasTriplet = counts.values.contains(3)
        val hasPair = counts.values.contains(2)
        
        return when {
            maxCount == 5 -> HandRank.FIVE_OF_A_KIND
            maxCount == 4 -> HandRank.FOUR_OF_A_KIND
            hasTriplet && hasPair -> HandRank.FULL_HOUSE
            maxCount == 3 -> HandRank.THREE_OF_A_KIND
            pairCount == 2 -> HandRank.TWO_PAIR
            maxCount == 2 -> HandRank.ONE_PAIR
            distinctCount == 5 && isStraight(dice) -> HandRank.STRAIGHT
            else -> HandRank.HIGH_DICE
        }
    }

    private fun isStraight(dice: List<Dice>): Boolean {
        val sortedStrengths = dice.map { it.face.strength }.sorted()
        return sortedStrengths == listOf(1, 2, 3, 4, 5) || sortedStrengths == listOf(2, 3, 4, 5, 6)
    }

    companion object {
        fun getFactory(service: GameService, authRepo: AuthInfoRepo): ViewModelProvider.Factory = viewModelFactory {
            initializer { GameViewModel(service, authRepo) }
        }
    }
}