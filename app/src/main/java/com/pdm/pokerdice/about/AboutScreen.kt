import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pdm.pokerdice.ui.theme.GenericTopAppBar
import com.pdm.pokerdice.ui.theme.RetroBackgroundEnd
import com.pdm.pokerdice.ui.theme.RetroBackgroundStart
import com.pdm.pokerdice.ui.theme.RetroGold
import com.pdm.pokerdice.ui.theme.RetroNavyDark
import com.pdm.pokerdice.ui.theme.RetroNavyLight

sealed class AboutNavigation {
    class Creator(
        val destination: String,
    ) : AboutNavigation()

    object GameRules : AboutNavigation()

    object ContactUs : AboutNavigation()
}

const val GAME_RULES = "game_rules_button"
const val CONTACT_US = "contact_us_button"
const val CREATOR1 = "creator1_button"
const val CREATOR2 = "creator2_button"
const val CREATOR3 = "creator3_button"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    modifier: Modifier,
    onNavigate: (AboutNavigation) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GenericTopAppBar(
                title = "About"
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(RetroBackgroundStart, RetroBackgroundEnd),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                "About Poker Dice",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RetroNavyLight
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "Game Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RetroGold
                    )
                    Text(
                        "Poker Dice is a game where players roll five dice " +
                            "to make poker-style combinations. Each player gets " +
                            "three rolls per turn to achieve the best hand possible.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Button(
                        onClick = { onNavigate(AboutNavigation.GameRules) },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.align(Alignment.End).testTag(GAME_RULES),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = RetroNavyDark
                        )
                    ) {
                        Text("Game Rules")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = RetroNavyLight
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "Authors",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RetroGold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Pedro Monteiro (51457)", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                        Button(
                            onClick = { onNavigate(AboutNavigation.Creator("https://github.com/pedrowlv")) },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.testTag(CREATOR1),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = RetroNavyDark
                            )
                        ) {
                            Text("GitHub")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("João Silva (51682)", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                        Button(
                            onClick = { onNavigate(AboutNavigation.Creator("https://github.com/JoaoSilvaIT")) },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.testTag(CREATOR2),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = RetroNavyDark
                            )
                        ) {
                            Text("GitHub")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Bernardo Jaco (51690)", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                        Button(
                            onClick = { onNavigate(AboutNavigation.Creator("https://github.com/jacaoo")) },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.testTag(CREATOR3),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = RetroNavyDark
                            )
                        ) {
                            Text("GitHub")
                        }
                    }
                }
            }

            Button(
                onClick = { onNavigate(AboutNavigation.ContactUs) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth().testTag(CONTACT_US),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = RetroNavyDark
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                    Text("Contact Us", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen(Modifier)
}
