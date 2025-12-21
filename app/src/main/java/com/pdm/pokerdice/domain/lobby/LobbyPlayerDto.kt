package com.pdm.pokerdice.domain.lobby

import com.pdm.pokerdice.domain.user.UserExternalInfo

data class LobbyPlayerDto(
    val id: Int,
    val name: String?,
) {
    fun toDomain() = UserExternalInfo(id, name ?: "Unknown", 0) // Balance not in LobbyPlayerDto?
}
