package com.pdm.pokerdice.domain.lobby

import android.os.Parcelable
import com.pdm.pokerdice.domain.user.UserExternalInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lobby(
    val id: Int,
    val name: String,
    val description: String,
    val host: UserExternalInfo,
    val settings: LobbySettings,
    val players: Set<UserExternalInfo>,
    val timeout: Long = 10L,
) : Parcelable
