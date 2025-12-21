package com.pdm.pokerdice.domain.game

import android.os.Parcelable
import com.pdm.pokerdice.domain.game.utilis.Face
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dice(
    val face: Face,
) : Parcelable
