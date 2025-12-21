package com.pdm.pokerdice.domain.user

data class MeOutputDto(
    val id: Int,
    val name: String,
    val email: String,
    val balance: Int,
) {
    fun toDomain(stats: UserStatistics) = User(id, name, email, "", balance, stats)
}
