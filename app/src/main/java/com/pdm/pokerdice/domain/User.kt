package com.pdm.pokerdice.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val uid : Int,
    val name : String,
    val email : String = "",
    val password: String = ""
) : Parcelable
