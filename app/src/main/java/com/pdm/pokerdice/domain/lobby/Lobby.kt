package com.pdm.pokerdice.domain.lobby

import android.os.Parcelable
import com.pdm.pokerdice.domain.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lobby (
    val lid : Int,
    val name: String,
    val description : String,
    val users : List<User>,
    val expectedPlayers : Int,
    val host : User,
    val rounds : Int
) : Parcelable