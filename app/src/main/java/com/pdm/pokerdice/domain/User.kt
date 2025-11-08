package com.pdm.pokerdice.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val uid : Int,
    val name : String,
    val email : String = "",
    val password: String = "" // For Milestone 3 fake auth, will be replaced with token in Milestone 5
) : Parcelable
