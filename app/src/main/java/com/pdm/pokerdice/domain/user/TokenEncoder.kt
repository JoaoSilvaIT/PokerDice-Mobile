package com.pdm.pokerdice.domain.user

interface TokenEncoder {
    fun createValidationInformation(token: String): TokenValidationInfo
}
