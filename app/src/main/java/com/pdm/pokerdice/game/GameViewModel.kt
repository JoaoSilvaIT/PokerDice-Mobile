package com.pdm.pokerdice.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.Hand
import com.pdm.pokerdice.domain.game.PlayerInGame
import com.pdm.pokerdice.domain.game.Round
import com.pdm.pokerdice.domain.game.Turn
import com.pdm.pokerdice.domain.game.utilis.Face
import com.pdm.pokerdice.domain.game.utilis.HandRank
import com.pdm.pokerdice.domain.game.utilis.State
import com.pdm.pokerdice.domain.game.utilis.defineHandRank
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserStatistics

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
        val currentHandRank: HandRank? = null
    ) : GameScreenState()
    data class RoundEnded(val winnerName: String, val winningHand: String) : GameScreenState()
    data class MatchEnded(val winnerName: String, val finalScore: Int) : GameScreenState()
}

class GameViewModel : ViewModel() {

    // Using mutableStateOf for Compose observability
    var screenState by mutableStateOf<GameScreenState>(GameScreenState.Loading)
        private set

    // Mock data initialization for Milestone 4
    init {
        startFakeGame()
    }

    fun submitAnte(amount: Int) {
        val currentState = screenState
        if (currentState is GameScreenState.SettingAnte) {
            val game = currentState.game
            val currentRound = game.currentRound!!
            
            // Deduct ante from players and add to pot
            // NOTE: M4 Fake Logic - Assuming everyone pays the ante instantly
            val updatedPlayers = game.players.map { 
                it.copy(currentBalance = it.currentBalance - amount) 
            }
            
            val totalPot = currentRound.pot + (amount * updatedPlayers.size)
            
            val updatedRound = currentRound.copy(
                ante = amount,
                pot = totalPot,
                players = updatedPlayers
            )
            
            val updatedGame = game.copy(
                currentRound = updatedRound,
                players = updatedPlayers
            )
            
            // Move to Playing State
            screenState = GameScreenState.Playing(
                game = updatedGame,
                isMyTurn = true, // Still my turn to roll after setting ante
                currentRollCount = 0,
                heldDiceIndexes = emptySet(),
                currentHandRank = null
            )
        }
    }

    fun rollDice() {
        val currentState = screenState
        if (currentState is GameScreenState.Playing && currentState.isMyTurn && currentState.currentRollCount < 3) {
            
            // If it's the first roll, we roll 5 new dice. 
            // Otherwise, we respect the held indexes.
            val currentDice = currentState.game.currentRound?.turn?.currentDice ?: emptyList()

            val newDiceList = if (currentDice.isEmpty()) {
                List(5) { Dice(Face.entries.random()) }
            } else {
                currentDice.mapIndexed { index, die ->
                    if (currentState.heldDiceIndexes.contains(index)) die
                    else Dice(Face.entries.random())
                }
            }

            // Calculate current hand rank
            val newHandRank = defineHandRank(Hand(newDiceList)).second
            
            val currentGame = currentState.game
            val currentRound = currentGame.currentRound!!
            val currentTurn = currentRound.turn
            
            val updatedTurn = currentTurn.copy(
                currentDice = newDiceList,
                rollsRemaining = 3 - (currentState.currentRollCount + 1)
            )
            
            val updatedRound = currentRound.copy(turn = updatedTurn)
            val updatedGame = currentGame.copy(currentRound = updatedRound)

            // Update state
            screenState = currentState.copy(
                game = updatedGame,
                currentRollCount = currentState.currentRollCount + 1
            )
            // Recalculate rank based on HELD dice (which are technically the ones we kept + new ones? 
            // User said: "calculate based on selected".
            // So we take the new dice list, filter by HELD indexes, and calculate rank.
            updateHandRank()
        }
    }

    fun toggleHoldDie(index: Int) {
        val currentState = screenState
        // Can only hold dice AFTER the first roll
        if (currentState is GameScreenState.Playing && 
            currentState.isMyTurn && 
            currentState.currentRollCount > 0 && 
            currentState.currentRollCount < 3
        ) {
            val currentHolds = currentState.heldDiceIndexes.toMutableSet()
            if (currentHolds.contains(index)) {
                currentHolds.remove(index)
            } else {
                currentHolds.add(index)
            }
            screenState = currentState.copy(heldDiceIndexes = currentHolds)
            updateHandRank()
        }
    }

    private fun updateHandRank() {
        val currentState = screenState
        if (currentState is GameScreenState.Playing) {
            val allDice = currentState.game.currentRound?.turn?.currentDice ?: emptyList()
            val heldDice = allDice.filterIndexed { index, _ -> currentState.heldDiceIndexes.contains(index) }
            
            val rank = if (heldDice.isEmpty()) null else calculatePartialRank(heldDice)
            
            screenState = currentState.copy(currentHandRank = rank)
        }
    }

    private fun calculatePartialRank(dice: List<Dice>): HandRank {
        val counts = dice.groupingBy { it.face }.eachCount()
        val maxCount = counts.values.maxOrNull() ?: 0
        val distinctCount = counts.size
        
        return when {
            maxCount == 5 -> HandRank.FIVE_OF_A_KIND
            maxCount == 4 -> HandRank.FOUR_OF_A_KIND
            maxCount == 3 && distinctCount == 2 -> HandRank.FULL_HOUSE // e.g. 3 K, 2 Q
            maxCount == 3 -> HandRank.THREE_OF_A_KIND
            maxCount == 2 && distinctCount == 2 && dice.size >= 4 -> HandRank.TWO_PAIR // e.g. 2 K, 2 Q (size 4) or 2K 2Q 1J (size 5)
            maxCount == 2 -> HandRank.ONE_PAIR
            distinctCount == 5 && isStraight(dice) -> HandRank.STRAIGHT
            else -> HandRank.HIGH_DICE
        }
    }

    private fun isStraight(dice: List<Dice>): Boolean {
        val sortedStrengths = dice.map { it.face.strength }.sorted()
        // Check for sequence (e.g., 1,2,3,4,5 or 2,3,4,5,6)
        // Strength: Nine=1 ... Ace=6
        // Straight requires 5 dice.
        return sortedStrengths == listOf(1, 2, 3, 4, 5) || sortedStrengths == listOf(2, 3, 4, 5, 6)
    }

    fun endTurn() {
         // Logic to pass turn to next player
         // For M4: Simulate opponent playing and then Round End
         screenState = GameScreenState.RoundEnded("Opponent", "Full House")
    }

    fun nextRound() {
        // Logic to start next round
        startFakeGame() // Reset to playing for demo
    }

    private fun startFakeGame() {
        // Fake Data Setup
        val me = PlayerInGame(1, "Me", 100, 0)
        val opponent = PlayerInGame(2, "Opponent", 100, 0)
        
        // Correct User instantiation
        val fakeUser = User(
            id = 1,
            name = "Me",
            email = "",
            password = "",
            balance = 100,
            statistics = UserStatistics(0, 0, 0, 0.0)
        )
        
        // Correct Turn instantiation - STARTING EMPTY
        val fakeTurn = Turn(
            player = fakeUser,
            rollsRemaining = 3,
            currentDice = emptyList()
        )

        // Correct Round instantiation
        val currentRound = Round(
            number = 1,
            firstPlayerIdx = 0,
            turn = fakeTurn,
            players = listOf(me, opponent),
            playerHands = emptyMap(),
            ante = 0, // Ante not set yet
            pot = 0,
            winners = emptyList(),
            gameId = 1
        )

        // Correct Game instantiation
        val fakeGame = Game(
            id = 1,
            lobbyId = 101,
            players = listOf(me, opponent),
            numberOfRounds = 3,
            state = State.RUNNING,
            currentRound = currentRound,
            startedAt = System.currentTimeMillis(),
            endedAt = null
        )
        
        // Start in SettingAnte mode
        screenState = GameScreenState.SettingAnte(fakeGame)
    }

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                GameViewModel()
            }
        }
    }
}
