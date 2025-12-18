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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.utilis.Face
import com.pdm.pokerdice.game.GameScreenState

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
        OpponentSection(balance = opponentBalance)

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
            
            if (state.currentRollCount > 0) {
                DiceRow(
                    dice = currentDice,
                    heldIndexes = state.heldDiceIndexes,
                    isMyTurn = state.isMyTurn,
                    onDieClick = onDieClick
                )
                
                state.currentHandRank?.let { rank ->
                    Text(
                        text = "Current Hand: ${formatHandRank(rank)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                // Initial state: show empty slots
                EmptyDiceRow()
            }
        }

        // Bottom: Controls
        GameControls(
            isMyTurn = state.isMyTurn,
            rollsCount = state.currentRollCount,
            onRollClick = onRollClick,
            onEndTurnClick = onEndTurnClick
        )
    }
}

@Composable
fun formatHandRank(rank: com.pdm.pokerdice.domain.game.utilis.HandRank): String {
    return rank.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
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
fun OpponentSection(balance: Int) {
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
        
        Text("Waiting...", style = MaterialTheme.typography.bodySmall)
        
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
    isMyTurn: Boolean,
    onDieClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dice.forEachIndexed { index, die ->
            DieView(
                face = die.face,
                isHeld = heldIndexes.contains(index),
                isClickable = isMyTurn,
                onClick = { onDieClick(index) }
            )
        }
    }
}

@Composable
fun DieView(
    face: Face,
    isHeld: Boolean,
    isClickable: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = if (isHeld) Color.Gray else Color.White,
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
        val text = when (face) {
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
            color = Color.Black
        )
    }
}

@Composable
fun GameControls(
    isMyTurn: Boolean,
    rollsCount: Int,
    onRollClick: () -> Unit,
    onEndTurnClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isMyTurn) {
            Button(
                onClick = onRollClick,
                enabled = rollsCount < 3,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(if (rollsCount == 0) "Roll Dice" else "Re-Roll Selected")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onEndTurnClick,
                enabled = rollsCount > 0, // Must roll at least once
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("End Turn / Show Hand")
            }
        } else {
            Text("Waiting for opponent...")
        }
    }
}