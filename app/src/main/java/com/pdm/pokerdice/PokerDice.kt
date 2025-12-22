package com.pdm.pokerdice

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.repo.AuthInfoPreferencesRepo
import com.pdm.pokerdice.service.GameService
import com.pdm.pokerdice.service.HomeService
import com.pdm.pokerdice.service.HomeServiceImpl
import com.pdm.pokerdice.service.HttpGameService
import com.pdm.pokerdice.service.HttpLobbyService
import com.pdm.pokerdice.service.HttpUserAuthService
import com.pdm.pokerdice.service.LobbyService
import com.pdm.pokerdice.service.UserAuthService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson

interface DependenciesContainer {
    val homeService: HomeService
    val lobbyService: LobbyService
    val gameService: GameService
    val authInfoRepo: AuthInfoRepo
    val authService: UserAuthService
}

class PokerDice :
    Application(),
    DependenciesContainer {
    private val ds: DataStore<Preferences> by preferencesDataStore(name = "auth_info")

    // HTTP Client (Shared) - Use 10.0.2.2 for Emulator
    private val httpClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                gson()
            }
            install(SSE)
            defaultRequest {
                url("http://10.0.2.2:8080/")
            //  url("https://furlable-terica-fluvial.ngrok-free.dev")
                contentType(ContentType.Application.Json)
            }
        }
    }

    override val homeService: HomeService by lazy { HomeServiceImpl() }
    override val authInfoRepo: AuthInfoRepo by lazy { AuthInfoPreferencesRepo(ds) }

    // REAL HTTP SERVICES
    override val lobbyService: LobbyService by lazy {
        HttpLobbyService(httpClient, authInfoRepo)
    }

    override val gameService: GameService by lazy {
        HttpGameService(httpClient, authInfoRepo)
    }

    override val authService: UserAuthService by lazy {
        HttpUserAuthService(httpClient, authInfoRepo)
    }
}
