import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
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

@Composable
fun AboutScreen(
    modifier: Modifier,
    onNavigate: (AboutNavigation) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            "About Poker Dice",
            // Use theme typography for consistency
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "Game Description",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Poker Dice is a game where players roll five dice " +
                        "to make poker-style combinations. Each player gets " +
                        "three rolls per turn to achieve the best hand possible.",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Button(
                    onClick = { onNavigate(AboutNavigation.GameRules) },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.align(Alignment.End).testTag(GAME_RULES),
                ) {
                    Text("Game Rules")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "Authors",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Pedro Monteiro (51457)", style = MaterialTheme.typography.bodyLarge)
                    Button(
                        onClick = { onNavigate(AboutNavigation.Creator("https://github.com/pedrowlv")) },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.testTag(CREATOR1),
                    ) {
                        Text("GitHub")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("João Silva (51682)", style = MaterialTheme.typography.bodyLarge)
                    Button(
                        onClick = { onNavigate(AboutNavigation.Creator("https://github.com/JoaoSilvaIT")) },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.testTag(CREATOR2),
                    ) {
                        Text("GitHub")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Bernardo Jaco (51690)", style = MaterialTheme.typography.bodyLarge)
                    Button(
                        onClick = { onNavigate(AboutNavigation.Creator("https://github.com/jacaoo")) },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.testTag(CREATOR3),
                    ) {
                        Text("GitHub")
                    }
                }
            }
        }

        Button(
            onClick = { onNavigate(AboutNavigation.ContactUs) },
            // Default button colors for consistent theme usage
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().testTag(CONTACT_US),
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

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen(Modifier)
}
