package com.pdm.pokerdice.domain.lobby

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LobbySettings(
    val numberOfRounds: Int,
    val minPlayers: Int,
    val maxPlayers: Int,
    // val timeout: Long? // Timeout em segundos para iniciar um game com minPlayers
) : Parcelable
