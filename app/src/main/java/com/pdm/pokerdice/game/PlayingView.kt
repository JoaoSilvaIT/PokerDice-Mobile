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
import kotlinx.coroutines.delay

@Composable
fun formatHandRank(rank: HandRank): String =
    rank.name
        .replace("_", " ")
        .lowercase()
        .replaceFirstChar { it.uppercase() }

@Composable
fun PlayingView(
    state: GameScreenState.Playing,
    onRollClick: () -> Unit,
    onHoldClick: () -> Unit,
    onDieClick: (Int) -> Unit,
    onEndTurnClick: () -> Unit,
) {
    // Determine Me vs Opponent
    val myId = state.myUserId
    val me = state.game.players.find { it.id == myId }
    val opponent = state.game.players.find { it.id != myId }

    val myBalance = me?.currentBalance ?: 0
    val opponentBalance = opponent?.currentBalance ?: 0
    val opponentName = opponent?.name ?: "Opponent"

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Top: Opponent Info
        OpponentSection(
            name = opponentName,
            balance = opponentBalance,
            dice = state.opponentDice,
            handRank = state.opponentHandRank,
        )

        // Middle Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GameHeader(state, myBalance)

            Text(
                text = if (state.isMyTurn) "Your Turn" else "Opponent's Turn",
                style = MaterialTheme.typography.headlineMedium,
                color = if (state.isMyTurn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            )

            if (state.isMyTurn) {
                Text("Rolls Left: ${3 - state.currentRollCount}")
            }

            // Dice Row Logic
            val lockedDice =
                state.game.currentRound
                    ?.turn
                    ?.currentDice ?: emptyList()
            val rolledDice = state.rolledDice

            // If we have no dice (start of game), show placeholders if it's our turn to roll?
            // Or just empty slots.
            val displayDice =
                if (lockedDice.isEmpty() && rolledDice.isEmpty() && state.currentRollCount == 0) {
                    List(5) { Dice(Face.ACE) } // Initial state
                } else {
                    lockedDice + rolledDice
                }

            // Check if valid to show dice
            if (state.currentRollCount > 0 || state.isRolling || lockedDice.isNotEmpty()) {
                if (state.isMyTurn && !state.isRolling) {
                    Text(
                        text = "Tap white dice to select",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }

                DiceRow(
                    dice = displayDice,
                    lockedCount = lockedDice.size,
                    heldIndexes = state.heldDiceIndexes,
                    isMyTurn = state.isMyTurn,
                    isRolling = state.isRolling,
                    onDieClick = onDieClick,
                )

                // ... (Dice Row Logic) ...
                // Rank
                state.currentHandRank?.let { rank ->
                    Text(
                        text = "Current Hand: ${formatHandRank(rank)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            } else {
                EmptyDiceRow()
            }
        }

        // Bottom: Controls
        val lockedCount =
            state.game.currentRound
                ?.turn
                ?.currentDice
                ?.size ?: 0
        val heldCount = state.heldDiceIndexes.size
        // Valid if Total Committed (Locked + Held) is 5.
        // Or if we have finished rolling (rollsCount == 0)? No, user said "not possible to finish turn if not have 5 dice held".
        // This implies strict 5 dice rule.
        val canEndTurn = (lockedCount + heldCount) == 5

        GameControls(
            isMyTurn = state.isMyTurn,
            isRolling = state.isRolling,
            rollsCount = state.currentRollCount,
            hasHeldDice = state.heldDiceIndexes.isNotEmpty(),
            canEndTurn = canEndTurn,
            onRollClick = onRollClick,
            onHoldClick = onHoldClick,
            onEndTurnClick = onEndTurnClick,
        )
    }
}

@Composable
fun DiceRow(
    dice: List<Dice>,
    lockedCount: Int,
    heldIndexes: Set<Int>, // Indexes relative to the ROLLED portion
    isMyTurn: Boolean,
    isRolling: Boolean,
    onDieClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        dice.forEachIndexed { index, die ->
            val isLocked = index < lockedCount
            val rolledIndex = index - lockedCount

            // It is held if it's NOT locked AND the adjusted index is in held set
            val isHeld = !isLocked && heldIndexes.contains(rolledIndex)

            // It spins if we are rolling AND it's not held AND not locked
            // (Locked dice don't spin. Held dice don't spin.)
            val isSpinning = isRolling && !isLocked && !isHeld

            DieView(
                face = die.face,
                isHeld = isHeld,
                isLocked = isLocked,
                isRolling = isSpinning,
                isClickable = isMyTurn && !isLocked,
                onClick = {
                    if (!isLocked) {
                        onDieClick(rolledIndex)
                    }
                },
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
    onClick: () -> Unit,
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

    val bgColor =
        when {
            isLocked -> Color.DarkGray
            isHeld -> Color.Gray
            else -> Color.White
        }

    Box(
        modifier =
            Modifier
                .size(60.dp)
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(8.dp),
                ).border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp),
                ).clickable(enabled = isClickable, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        val text =
            when (faceDisplay) {
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
            color = if (isLocked || isHeld) Color.White else Color.Black,
        )
    }
}

@Composable
fun GameControls(
    isMyTurn: Boolean,
    isRolling: Boolean,
    rollsCount: Int,
    hasHeldDice: Boolean,
    canEndTurn: Boolean,
    onRollClick: () -> Unit,
    onHoldClick: () -> Unit,
    onEndTurnClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isMyTurn) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    onClick = onHoldClick,
                    enabled = hasHeldDice && !isRolling,
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                ) {
                    Text("Hold Selected")
                }

                Button(
                    onClick = onRollClick,
                    enabled = rollsCount < 3 && !isRolling,
                    modifier = Modifier.weight(1f).padding(start = 4.dp),
                ) {
                    Text(if (isRolling) "Rolling..." else "Roll")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEndTurnClick,
                enabled = canEndTurn && !isRolling,
                modifier = Modifier.fillMaxWidth(0.9f),
            ) {
                Text("End Turn / Show Hand")
            }
        } else {
            Text("Waiting for opponent...")
        }
    }
}

@Composable
fun EmptyDiceRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        repeat(5) {
            Box(
                modifier =
                    Modifier
                        .size(60.dp)
                        .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            )
        }
    }
}

@Composable
fun OpponentSection(
    name: String,
    balance: Int,
    dice: List<Dice> = emptyList(),
    handRank: HandRank? = null,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("Balance: $balance", style = MaterialTheme.typography.bodyMedium)
        }

        Text(if (dice.isNotEmpty()) "Rolled:" else "Waiting...", style = MaterialTheme.typography.bodySmall)

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (dice.isNotEmpty()) {
                dice.forEach { die ->
                    Box(
                        modifier =
                            Modifier
                                .size(30.dp)
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        val text =
                            when (die.face) {
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
                        modifier =
                            Modifier
                                .size(30.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center,
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
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
fun GameHeader(
    state: GameScreenState.Playing,
    myBalance: Int,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Me", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text("Balance: $myBalance", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Round: ${state.game.currentRound?.number ?: 1}")
            Text("Pot: ${state.game.currentRound?.pot ?: 0}", fontWeight = FontWeight.Bold)
        }
    }
}
