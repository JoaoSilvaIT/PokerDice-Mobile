package com.pdm.pokerdice.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val uid : Int,
    val name : String,
    val email : String = ""
) : Parcelable

val users1st = mutableListOf(
    User(1, "Alice"),
    User(2, "Bob"),
    User(3, "Charlie"),
    User(4, "Diana")
)

val users2nd = mutableListOf(
    User(5, "Eve"),
    User(6, "Frank"),
    User(7, "Grace"),
    User(8, "Hank"))

val users3rd = mutableListOf(
    User(9, "Ivy"),
    User(10, "Jack"),
    User(11, "Kathy"),
    User(12, "Leo"))

val users4th = mutableListOf(
    User(13, "Mona"),
    User(14, "Nate"),
    User(15, "Olivia"),
    User(16, "Paul"))