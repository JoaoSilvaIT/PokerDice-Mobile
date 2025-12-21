package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.user.UserExternalInfo

interface Service {
    suspend fun getLoggedUser(): UserExternalInfo?
}
