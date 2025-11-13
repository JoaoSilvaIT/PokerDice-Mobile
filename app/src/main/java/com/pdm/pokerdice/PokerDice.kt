package com.pdm.pokerdice

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.pdm.pokerdice.login_signup.AuthInfoRepo
import com.pdm.pokerdice.login_signup.FakeLoginService
import com.pdm.pokerdice.login_signup.FakeSignUpService
import com.pdm.pokerdice.login_signup.LoginService
import com.pdm.pokerdice.login_signup.SignUpService
import com.pdm.pokerdice.login_signup.infrastructure.AuthInfoPreferencesRepo
import com.pdm.pokerdice.repo.mem.ManagerinMem
import com.pdm.pokerdice.repo.mem.RepoLobbyInMem
import com.pdm.pokerdice.repo.mem.RepoUserInMem
import com.pdm.pokerdice.repo.tm.Manager
import com.pdm.pokerdice.service.FakeLobbyService
import com.pdm.pokerdice.service.LobbyService

interface DependenciesContainer {
    val lobbyService: LobbyService
    val signUpService: SignUpService
    val authInfoRepo : AuthInfoRepo
}
class PokerDice : DependenciesContainer, Application() {

    private val manager = ManagerinMem()

    private val ds: DataStore<Preferences> by preferencesDataStore(name = "auth_info")

    override val authInfoRepo: AuthInfoRepo by lazy { AuthInfoPreferencesRepo(ds) }

    override val signUpService: SignUpService by lazy { FakeSignUpService(manager) }

    override val lobbyService: LobbyService by lazy { FakeLobbyService(manager) }
}
