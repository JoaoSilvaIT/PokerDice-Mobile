package com.pdm.pokerdice.game

import com.pdm.pokerdice.domain.game.Dice
import com.pdm.pokerdice.domain.game.utilis.Face
import com.pdm.pokerdice.domain.game.utilis.HandRank

fun faceToCharString(face: Face): String {
    return when (face) {
        Face.ACE -> "A"
        Face.KING -> "K"
        Face.QUEEN -> "Q"
        Face.JACK -> "J"
        Face.TEN -> "T"
        Face.NINE -> "9"
    }
}

fun calculatePartialRank(dice: List<Dice>): HandRank {
    val counts = dice.groupingBy { it.face }.eachCount()
    val maxCount = counts.values.maxOrNull() ?: 0
    val pairCount = counts.values.count { it == 2 }
    val distinctCount = counts.size
    val hasTriplet = counts.values.contains(3)
    val hasPair = counts.values.contains(2)

    return when {
        maxCount == 5 -> HandRank.FIVE_OF_A_KIND
        maxCount == 4 -> HandRank.FOUR_OF_A_KIND
        hasTriplet && hasPair -> HandRank.FULL_HOUSE
        maxCount == 3 -> HandRank.THREE_OF_A_KIND
        pairCount == 2 -> HandRank.TWO_PAIR
        maxCount == 2 -> HandRank.ONE_PAIR
        distinctCount == 5 && isStraight(dice) -> HandRank.STRAIGHT
        else -> HandRank.HIGH_DICE
    }
}

private fun isStraight(dice: List<Dice>): Boolean {
    val sortedStrengths = dice.map { it.face.strength }.sorted()
    return sortedStrengths == listOf(1, 2, 3, 4, 5) || sortedStrengths == listOf(2, 3, 4, 5, 6)
}

fun parseFace(faceStr: String): Face {
    return when (faceStr.uppercase()) {
        "A", "ACE" -> Face.ACE
        "K", "KING" -> Face.KING
        "Q", "QUEEN" -> Face.QUEEN
        "J", "JACK" -> Face.JACK
        "T", "TEN" -> Face.TEN
        "9", "NINE" -> Face.NINE
        else -> Face.ACE
    }
}