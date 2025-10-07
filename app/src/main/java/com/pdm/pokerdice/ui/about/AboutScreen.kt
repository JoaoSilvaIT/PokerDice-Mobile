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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


sealed class AboutNavigation {
    class Creator(val destination: String) : AboutNavigation()
    object GameRules : AboutNavigation()
    object ContactUs : AboutNavigation()
}

const val GAME_RULES = "game_rules_button"
const val CONTACT_US = "contact_us_button"
const val CREATOR1 = "creator1_button"
const val CREATOR2 = "creator2_button"
const val CREATOR3 = "creator3_button"

@Composable
fun AboutScreen(modifier: Modifier, onNavigate: (AboutNavigation) -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ){
        Text("About Poker Dice",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary // MUDAR SE QUISEREM
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Game Description",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Poker Dice is a game where players roll five dice " +
                            "to make poker-style combinations. Each player gets " +
                            "three rolls per turn to achieve the best hand possible.",
                    fontSize = 16.sp
                )
                Button(
                    onClick = { onNavigate(AboutNavigation.GameRules) },
                    modifier = Modifier.align(Alignment.End).testTag(GAME_RULES)
                ) {
                    Text("Game Rules")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Authors",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Pedro Monteiro (51457)")
                    Button(onClick = { onNavigate(AboutNavigation.Creator("https://github.com/pedrowlv")) },
                        modifier = Modifier.testTag(CREATOR1)
                    ) {
                        Text("GitHub")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("João Silva (51682)")
                    Button(onClick = { onNavigate(AboutNavigation.Creator("https://github.com/JoaoSilvaIT")) },
                        modifier = Modifier.testTag(CREATOR2)
                        ) {
                        Text("GitHub")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Bernardo Jaco (51690)")
                    Button(onClick = { onNavigate(AboutNavigation.Creator("https://github.com/jacaoo")) },
                        modifier = Modifier.testTag(CREATOR3)
                        ) {
                        Text("GitHub")
                    }
                }
            }
        }

        Button(
            onClick = { onNavigate(AboutNavigation.ContactUs) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth().testTag(CONTACT_US)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Email, contentDescription = "Email")
                Text("Contact Us", fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen(Modifier)
}
