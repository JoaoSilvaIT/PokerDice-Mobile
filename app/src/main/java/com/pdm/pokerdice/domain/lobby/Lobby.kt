package com.pdm.pokerdice.domain.lobby

import android.os.Parcelable
import com.pdm.pokerdice.domain.user.UserExternalInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lobby (
    val id : Int,
    val name: String,
    val description : String,
    val minPlayers : Int,
    val maxPlayers : Int,
    val players : Set<UserExternalInfo>,
    val host : UserExternalInfo
) : Parcelable