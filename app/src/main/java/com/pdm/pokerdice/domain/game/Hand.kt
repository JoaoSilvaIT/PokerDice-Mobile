package com.pdm.pokerdice.domain.game

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hand(
    val dices : List<Dice>
) : Parcelable
