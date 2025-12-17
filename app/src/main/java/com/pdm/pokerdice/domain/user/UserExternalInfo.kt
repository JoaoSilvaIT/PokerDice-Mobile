package com.pdm.pokerdice.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  UserExternalInfo(
    val id : Int,
    val name : String,
    val balance : Int
) : Parcelable