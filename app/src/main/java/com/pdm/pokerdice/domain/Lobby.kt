package com.pdm.pokerdice.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lobby (
    val lid : Int,
    val name: String,
    val description : String,
    val users : MutableList<User>,
    val limit : Int
) : Parcelable

//Vai ser mudado, só para ficar de exemplo
val lobbies = mutableListOf(
    Lobby(1, "High Rollers", "", users1st, 2),
    Lobby(2, "Casual Dice", "", users2nd, 4),
    Lobby(3, "Late Night Lobby", "", users3rd, 6),
    Lobby(4, "Fast Game", "", users4th, 10)
)