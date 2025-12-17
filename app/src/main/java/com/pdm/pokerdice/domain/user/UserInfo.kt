package com.pdm.pokerdice.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo (
    val id: Int,
    val token: String
): Parcelable