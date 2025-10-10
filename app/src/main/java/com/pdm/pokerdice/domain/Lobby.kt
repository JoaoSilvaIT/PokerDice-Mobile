package com.pdm.pokerdice.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lobby (
    val lid : Int,
    val name: String,
    val description : String,
    val users : List<User>,
    val maxUsers : Int,
    val host : User,
    val rounds : Int
) : Parcelable