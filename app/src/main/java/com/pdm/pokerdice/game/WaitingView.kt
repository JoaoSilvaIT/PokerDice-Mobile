package com.pdm.pokerdice.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pdm.pokerdice.domain.game.Game
import com.pdm.pokerdice.domain.game.PlayerInGame
import com.pdm.pokerdice.domain.game.utilis.State
import com.pdm.pokerdice.ui.theme.PokerDiceTheme

@Composable
fun WaitingView(
    game: Game,
    isHost: Boolean = false,
    onStart: () -> Unit,
) {
    // Cores baseadas na imagem
    val backgroundColor = Color(0xFF36596D)
    val borderColor = Color(0xFFD4A574)
    val cardColor = Color(0xFF2C3E4F)
    val playerIconColor = Color(0xFFD4A574)
    val textColor = Color(0xFFE8D5B5)

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(3.dp, borderColor, RoundedCornerShape(16.dp))
                    .background(backgroundColor.copy(alpha = 0.95f), RoundedCornerShape(16.dp))
                    .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "WAITING FOR GAME TO START",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
                letterSpacing = 2.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Game Info
            Text(
                text = "Game #${game.id}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
            )

            Text(
                text = "${game.numberOfRounds} Rounds",
                fontSize = 20.sp,
                color = textColor,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Players section
            Text(
                text = "Players (${game.players.size})",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
            )

            // Lista de jogadores
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                game.players.forEachIndexed { index, player ->
                    PlayerCard(
                        playerName = player.name,
                        isHost = index == 0,
                        iconColor = playerIconColor,
                        cardColor = cardColor,
                        textColor = textColor,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isHost) {
                Text(
                    text = "Waiting for host to start the game...",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = textColor.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                )
            } else {
                Button(onClick = onStart) {
                    Text("START GAME 🎮")
                }
            }
        }
    }
}

@Composable
fun PlayerCard(
    playerName: String,
    isHost: Boolean,
    iconColor: Color,
    cardColor: Color,
    textColor: Color,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(cardColor, RoundedCornerShape(12.dp))
                .border(2.dp, iconColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        // Ícone do jogador
        Box(
            modifier =
                Modifier
                    .size(50.dp)
                    .background(iconColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = playerName.firstOrNull()?.uppercase() ?: "U",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        // Nome do jogador
        Text(
            text = if (isHost) "$playerName (Host)" else playerName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
        )
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun WaitingViewPreview() {
    PokerDiceTheme {
        WaitingView(
            game =
                Game(
                    id = 1,
                    lobbyId = 1,
                    players =
                        listOf(
                            PlayerInGame(
                                id = 1,
                                name = "User1",
                                currentBalance = 1000,
                                moneyWon = 0,
                            ),
                            PlayerInGame(
                                id = 2,
                                name = "User2",
                                currentBalance = 1000,
                                moneyWon = 0,
                            ),
                        ),
                    numberOfRounds = 2,
                    state = State.WAITING,
                    currentRound = null,
                    startedAt = System.currentTimeMillis(),
                    endedAt = null,
                ),
            isHost = true,
            {},
        )
    }
}
