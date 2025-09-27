package com.pdm.pokerdice.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lobby (
    val lid : Int,
    val name: String,
    val description : String,
    val users : MutableList<User>
) : Parcelable

//Vai ser mudado, só para ficar de exemplo
val lobbies = mutableListOf(
    Lobby(1, "High Rollers", "", users1st),
    Lobby(2, "Casual Dice", "", users2nd),
    Lobby(3, "Late Night Lobby", "", users3rd),
    Lobby(4, "Fast Game", "", users4th)
)