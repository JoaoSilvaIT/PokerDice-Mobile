package com.pdm.pokerdice.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String = "",
    val password: String = "",
    val balance: Int,
    val statistics: UserStatistics,
) : Parcelable
