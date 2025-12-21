package com.pdm.pokerdice.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pdm.pokerdice.domain.user.AuthInfo
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Implementation of AuthInfoRepo that uses DataStore to store authentication information.
 */

class AuthInfoPreferencesRepo(
    private val store: DataStore<Preferences>,
) : AuthInfoRepo {
    private val userId: Preferences.Key<Int> = intPreferencesKey(name = "user_id")

    private val authTokenKey: Preferences.Key<String> = stringPreferencesKey(name = "auth_token")

    override val authInfo: Flow<AuthInfo?>
        get() =
            store.data.map { preferences ->
                preferences.toAuthInfo()
            }

    override suspend fun clearAuthInfo() {
        store.edit { it.clear() }
    }

    override suspend fun getAuthInfo(): AuthInfo? {
        val preferences: Preferences = store.data.first()
        return preferences.toAuthInfo()
    }

    override suspend fun saveAuthInfo(authInfo: AuthInfo) {
        store.edit { preferences ->
            preferences[userId] = authInfo.userId
            preferences[authTokenKey] = authInfo.authToken
        }
    }

    fun Preferences.toAuthInfo(): AuthInfo? =
        this[userId]?.let {
            val token = this[authTokenKey] ?: return null
            AuthInfo(it, token)
        }
}
