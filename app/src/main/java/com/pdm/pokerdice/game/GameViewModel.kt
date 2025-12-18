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
}

class GameViewModel : ViewModel() {

    var screenState by mutableStateOf<GameScreenState>(GameScreenState.Loading)
        private set

    private var currentGame: Game? = null

    init {
        startFakeGame()
    }

    fun submitAnte(amount: Int) {
        val game = currentGame ?: return
        val currentRound = game.currentRound!!
        
        val updatedPlayers = game.players.map { 
            it.copy(currentBalance = it.currentBalance - amount) 
        }
        
        val totalPot = currentRound.pot + (amount * updatedPlayers.size)
        
        val updatedRound = currentRound.copy(
            ante = amount,
            pot = totalPot,
            players = updatedPlayers
        )
        
        currentGame = game.copy(
            currentRound = updatedRound,
            players = updatedPlayers
        )
        
        screenState = GameScreenState.Playing(
            game = currentGame!!,
            isMyTurn = true,
            currentRollCount = 0,
            heldDiceIndexes = emptySet(),
            lockedDiceIndexes = emptySet(),
            currentHandRank = null,
            isRolling = false,
            opponentDice = emptyList(),
            opponentHandRank = null
        )
    }

    fun rollDice() {
        val currentState = screenState
        if (currentState is GameScreenState.Playing && 
            currentState.isMyTurn && 
            currentState.currentRollCount < 3 && 
            !currentState.isRolling
        ) {
            viewModelScope.launch {
                screenState = currentState.copy(isRolling = true)
                delay(1000)

                val currentDice = currentState.game.currentRound?.turn?.currentDice ?: emptyList()
                val newDiceList = if (currentDice.isEmpty()) {
                    List(5) { Dice(Face.values().random()) }
                } else {
                    currentDice.mapIndexed { index, die ->
                        if (currentState.heldDiceIndexes.contains(index)) die
                        else Dice(Face.values().random())
                    }
                }

                val currentRound = currentGame?.currentRound!!
                val updatedTurn = currentRound.turn.copy(
                    currentDice = newDiceList,
                    rollsRemaining = 3 - (currentState.currentRollCount + 1)
                )
                
                val updatedRound = currentRound.copy(turn = updatedTurn)
                currentGame = currentGame?.copy(currentRound = updatedRound)

                val newLocked = currentState.heldDiceIndexes

                screenState = currentState.copy(
                    game = currentGame!!,
                    currentRollCount = currentState.currentRollCount + 1,
                    isRolling = false,
                    lockedDiceIndexes = newLocked
                )
                updateHandRank()
            }
        }
    }

    fun toggleHoldDie(index: Int) {
        val currentState = screenState
        if (currentState is GameScreenState.Playing && 
            currentState.isMyTurn && 
            currentState.currentRollCount > 0
        ) {
            if (currentState.lockedDiceIndexes.contains(index)) {
                return 
            }

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
            
            val diceToEvaluate = if (currentState.currentRollCount >= 3) {
                allDice
            } else {
                allDice.filterIndexed { index, _ -> currentState.heldDiceIndexes.contains(index) }
            }

            val rank = if (diceToEvaluate.isEmpty()) null else calculatePartialRank(diceToEvaluate)
            screenState = currentState.copy(currentHandRank = rank)
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

    fun endTurn() {
        val currentState = screenState
        if (currentState !is GameScreenState.Playing) return
        
        val game = currentGame ?: return
        
        viewModelScope.launch {
            try {
                screenState = currentState.copy(isMyTurn = false)
                
                delay(1500) 
                
                val opponentHandDice = List(5) { Dice(Face.values().random()) }
                val opponentRank = calculatePartialRank(opponentHandDice)
                
                screenState = currentState.copy(
                    opponentDice = opponentHandDice,
                    opponentHandRank = opponentRank,
                    isMyTurn = false
                )
                
                delay(3000) 
                
                val myDice = game.currentRound?.turn?.currentDice ?: emptyList()
                val myRank = calculatePartialRank(myDice)
                
                val myHandValue = myRank.strength
                val opponentHandValue = opponentRank.strength
                
                val winnerName: String
                val winningHandName: String
                val pot = game.currentRound?.pot ?: 0
                
                var updatedPlayers = game.players
                
                if (myHandValue >= opponentHandValue) {
                    winnerName = "Me"
                    winningHandName = myRank.name
                    updatedPlayers = game.players.map { 
                        if (it.id == 1) it.copy(currentBalance = it.currentBalance + pot) else it
                    }
                } else {
                    winnerName = "Opponent"
                    winningHandName = opponentRank.name
                    updatedPlayers = game.players.map { 
                        if (it.id == 2) it.copy(currentBalance = it.currentBalance + pot) else it
                    }
                }
                
                currentGame = game.copy(players = updatedPlayers)
                
                screenState = GameScreenState.RoundEnded(
                    winnerName = winnerName, 
                    winningHand = formatHandName(winningHandName)
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                screenState = GameScreenState.RoundEnded("Error", "Check Logcat")
            }
        }
    }
    
    private fun formatHandName(name: String) = name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }

    fun nextRound() {
        val game = currentGame ?: return
        val nextRoundNumber = (game.currentRound?.number ?: 0) + 1
        
        if (nextRoundNumber > game.numberOfRounds) {
            val winner = game.players.maxByOrNull { it.currentBalance }
            screenState = GameScreenState.MatchEnded(
                winnerName = winner?.name ?: "Draw",
                finalScore = winner?.currentBalance ?: 0
            )
        } else {
            // Alternate starting player: if current was 0, next is 1, and vice-versa
            val nextStartingPlayerIdx = 1 - (game.currentRound?.firstPlayerIdx ?: 0)
            
            val newRound = game.currentRound!!.copy(
                number = nextRoundNumber,
                firstPlayerIdx = nextStartingPlayerIdx,
                pot = 0,
                ante = 0,
                turn = game.currentRound!!.turn.copy(currentDice = emptyList(), rollsRemaining = 3)
            )
            currentGame = game.copy(currentRound = newRound)
            
            // If nextStartingPlayer is Me (0), go to SettingAnte. 
            // If it's Opponent (1), simulate them setting ante and start Playing.
            if (nextStartingPlayerIdx == 0) {
                screenState = GameScreenState.SettingAnte(currentGame!!)
            } else {
                // Fake Opponent sets ante (e.g., 10) and starts playing
                submitAnte(10) 
            }
        }
    }

    private fun startFakeGame() {
        val me = PlayerInGame(1, "Me", 100, 0)
        val opponent = PlayerInGame(2, "Opponent", 100, 0)
        val fakeUser = User(1, "Me", "", "", 100, UserStatistics(0, 0, 0, 0.0))
        val fakeTurn = Turn(player = fakeUser, rollsRemaining = 3, currentDice = emptyList())

        val currentRound = Round(
            number = 1, firstPlayerIdx = 0, turn = fakeTurn,
            players = listOf(me, opponent), playerHands = emptyMap(),
            ante = 0, pot = 0, winners = emptyList(), gameId = 1
        )

        currentGame = Game(
            id = 1, lobbyId = 101, players = listOf(me, opponent),
            numberOfRounds = 3,
            state = State.RUNNING, currentRound = currentRound,
            startedAt = System.currentTimeMillis(), endedAt = null
        )
        
        screenState = GameScreenState.SettingAnte(currentGame!!)
    }

    companion object {
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer { GameViewModel() }
        }
    }
}