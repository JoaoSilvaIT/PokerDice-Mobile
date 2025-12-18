package com.pdm.pokerdice.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.utilis.Face
import com.pdm.pokerdice.domain.game.utilis.HandRank
import com.pdm.pokerdice.game.GameScreenState
import kotlinx.coroutines.delay

@Composable
fun formatHandRank(rank: HandRank): String {
    return rank.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}

@Composable
fun PlayingView(
    state: GameScreenState.Playing,
    onRollClick: () -> Unit,
    onDieClick: (Int) -> Unit,
    onEndTurnClick: () -> Unit
) {
    // Determine Me vs Opponent (Fake data logic: Me=1)
    val myId = 1
    val me = state.game.players.find { it.id == myId }
    val opponent = state.game.players.find { it.id != myId }
    
    val myBalance = me?.currentBalance ?: 0
    val opponentBalance = opponent?.currentBalance ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top: Opponent Info
        OpponentSection(
            balance = opponentBalance,
            dice = state.opponentDice,
            handRank = state.opponentHandRank // Added to show opponent's strength
        )

        // Middle Info: Round & Pot & Dice Area
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GameHeader(state, myBalance)
            
            Text(
                text = if (state.isMyTurn) "Your Turn" else "Opponent's Turn",
                style = MaterialTheme.typography.headlineMedium,
                color = if (state.isMyTurn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
            
            if (state.isMyTurn) {
                Text("Rolls Left: ${3 - state.currentRollCount}")
            }

            // Dice Row
            val currentDice = state.game.currentRound?.turn?.currentDice ?: emptyList()
            
            // Logic to show dice
            if (state.currentRollCount > 0 || state.isRolling) {
                val displayDice = if (currentDice.isEmpty()) List(5) { Dice(Face.ACE) } else currentDice

                DiceRow(
                    dice = displayDice,
                    heldIndexes = state.heldDiceIndexes,
                    lockedIndexes = state.lockedDiceIndexes,
                    isMyTurn = state.isMyTurn && !state.isRolling,
                    isRolling = state.isRolling,
                    onDieClick = onDieClick
                )
                
                // Show player's current rank
                if (state.currentRollCount > 0 && !state.isRolling) {
                    state.currentHandRank?.let { rank ->
                        Text(
                            text = "Current Hand: ${formatHandRank(rank)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                // Initial state: show empty slots
                EmptyDiceRow()
            }
        }

        // Bottom: Controls
        GameControls(
            isMyTurn = state.isMyTurn,
            isRolling = state.isRolling,
            rollsCount = state.currentRollCount,
            isAllHeld = state.heldDiceIndexes.size == 5,
            onRollClick = onRollClick,
            onEndTurnClick = onEndTurnClick
        )
    }
}

@Composable
fun EmptyDiceRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(5) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun OpponentSection(balance: Int, dice: List<Dice> = emptyList(), handRank: HandRank? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Opponent", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("Balance: $balance", style = MaterialTheme.typography.bodyMedium)
        }
        
        Text(if (dice.isNotEmpty()) "Rolled:" else "Waiting...", style = MaterialTheme.typography.bodySmall)
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (dice.isNotEmpty()) {
                dice.forEach { die ->
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val text = when (die.face) {
                            Face.ACE -> "A"
                            Face.KING -> "K"
                            Face.QUEEN -> "Q"
                            Face.JACK -> "J"
                            Face.TEN -> "10"
                            Face.NINE -> "9"
                        }
                        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("?", fontSize = 14.sp)
                    }
                }
            }
        }

        // Display Opponent's Hand Rank if available
        handRank?.let {
            Text(
                text = "Hand: ${formatHandRank(it)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun GameHeader(state: GameScreenState.Playing, myBalance: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Me", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("Balance: $myBalance", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Round: ${state.game.currentRound?.number ?: 1}")
            Text("Pot: ${state.game.currentRound?.pot ?: 0}", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DiceRow(
    dice: List<Dice>,
    heldIndexes: Set<Int>,
    lockedIndexes: Set<Int>,
    isMyTurn: Boolean,
    isRolling: Boolean,
    onDieClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dice.forEachIndexed { index, die ->
            val isHeld = heldIndexes.contains(index)
            val isLocked = lockedIndexes.contains(index)
            
            DieView(
                face = die.face,
                isHeld = isHeld,
                isLocked = isLocked,
                isRolling = isRolling && !isHeld, 
                isClickable = isMyTurn && !isLocked, 
                onClick = { onDieClick(index) }
            )
        }
    }
}

@Composable
fun DieView(
    face: Face,
    isHeld: Boolean,
    isLocked: Boolean,
    isRolling: Boolean,
    isClickable: Boolean,
    onClick: () -> Unit
) {
    // Animation Logic
    var animatedFace by remember { mutableStateOf(face) }

    LaunchedEffect(isRolling) {
        if (isRolling) {
            while (true) {
                animatedFace = Face.values().random()
                delay(75) 
            }
        }
    }

    val faceDisplay = if (isRolling) animatedFace else face
    
    val bgColor = when {
        isLocked -> Color.DarkGray
        isHeld -> Color.Gray
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = bgColor,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = isClickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        val text = when (faceDisplay) {
            Face.ACE -> "A"
            Face.KING -> "K"
            Face.QUEEN -> "Q"
            Face.JACK -> "J"
            Face.TEN -> "10"
            Face.NINE -> "9"
        }
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isLocked || isHeld) Color.White else Color.Black
        )
    }
}

@Composable
fun GameControls(
    isMyTurn: Boolean,
    isRolling: Boolean,
    rollsCount: Int,
    isAllHeld: Boolean,
    onRollClick: () -> Unit,
    onEndTurnClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isMyTurn) {
            Button(
                onClick = onRollClick,
                enabled = rollsCount < 3 && !isRolling && !isAllHeld,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(if (isRolling) "Rolling..." else if (rollsCount == 0) "Roll Dice" else "Re-Roll Selected")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onEndTurnClick,
                enabled = isAllHeld && !isRolling,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("End Turn / Show Hand")
            }
        } else {
            Text("Waiting for opponent...")
        }
    }
}