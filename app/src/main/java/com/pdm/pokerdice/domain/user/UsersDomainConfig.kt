package com.pdm.pokerdice.domain.user

import java.time.Duration

data class UsersDomainConfig(
    val tokenSizeInBytes: Int,
    val tokenTtl: Duration,
    val tokenRollingTtl: Duration,
    val maxTokensPerUser: Int,
    val minPasswordLength: Int,
) {
    init {
        require(tokenSizeInBytes > 0)
        require(tokenTtl > Duration.ZERO)
        require(tokenRollingTtl > Duration.ZERO)
        require(maxTokensPerUser > 0)
        require(minPasswordLength > 0)
    }
}
