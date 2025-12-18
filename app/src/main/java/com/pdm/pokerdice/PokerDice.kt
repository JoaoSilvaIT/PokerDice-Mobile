package com.pdm.pokerdice

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pdm.pokerdice.service.HomeService
import com.pdm.pokerdice.service.HomeServiceImpl
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.Sha256TokenEncoder
import com.pdm.pokerdice.domain.user.UsersDomainConfig
import com.pdm.pokerdice.service.GameService
import com.pdm.pokerdice.repo.AuthInfoPreferencesRepo
import com.pdm.pokerdice.repo.mem.TransactionManagerInMem
import com.pdm.pokerdice.service.FakeGameService
import com.pdm.pokerdice.service.FakeLobbyService
import com.pdm.pokerdice.service.LobbyService
import com.pdm.pokerdice.service.UserAuthService
import java.time.Clock
import java.time.Duration

interface DependenciesContainer {
    val homeService : HomeService
    val lobbyService: LobbyService

    val gameService: GameService
    val authInfoRepo : AuthInfoRepo

    val authService : UserAuthService
}
class PokerDice : DependenciesContainer, Application() {
    private val manager = TransactionManagerInMem()
    private val ds: DataStore<Preferences> by preferencesDataStore(name = "auth_info")

    override val homeService: HomeService by lazy { HomeServiceImpl() }
    override val authInfoRepo: AuthInfoRepo by lazy { AuthInfoPreferencesRepo(ds) }

    override val lobbyService: LobbyService by lazy { FakeLobbyService(manager, authInfoRepo) }

    fun tokenEncoder() = Sha256TokenEncoder()

    fun clock(): Clock = Clock.systemUTC()

    fun usersDomainConfig() =
        UsersDomainConfig(
            tokenSizeInBytes = 256 / 8,
            tokenTtl = Duration.ofHours(24),
            tokenRollingTtl = Duration.ofHours(1),
            maxTokensPerUser = 3,
            minPasswordLength = 2,
        )

    override val authService: UserAuthService by lazy {
        UserAuthService(
            tokenEncoder = tokenEncoder(),
            config = usersDomainConfig(),
            trxManager = manager,
            clock = clock()
        )
    }

    override val gameService: GameService by lazy { FakeGameService(manager, authInfoRepo) }
}
