PokerDice (Android client)

UI: Kotlin + Jetpack Compose, multiple screens/flows (login, home, lobbies list, lobby screen, game screen, profile, about).

Networking: Ktor HTTP client; uses SSE plugin for lobby events; uses polling for lobbies list and game state.

Persistence: Android DataStore stores auth/session info.

Architecture: MVVM-style (ViewModels), plus a lightweight dependency container pattern for services.

Game UX: dice rolling + holding logic on the client side, plus API calls to advance turns/rounds and update state.

VIDEO :
https://iplx-my.sharepoint.com/:v:/g/personal/51682_alunos_isel_ipl_pt/IQDGlxbtAOCIS4SlFo6k7JiOAaLHqpjXlwnhOJTIx8ZuTro?e=nJssM5
