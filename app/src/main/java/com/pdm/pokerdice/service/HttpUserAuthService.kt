package com.pdm.pokerdice.service

import com.pdm.pokerdice.domain.user.AuthInfo
import com.pdm.pokerdice.domain.user.AuthInfoRepo
import com.pdm.pokerdice.domain.user.TokenExternalInfo
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.user.UserExternalInfo
import com.pdm.pokerdice.domain.user.UserStatistics
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.domain.utilis.failure
import com.pdm.pokerdice.domain.utilis.success
import com.pdm.pokerdice.repository.http.model.LoginInputDto
import com.pdm.pokerdice.repository.http.model.SignUpInputDto
import com.pdm.pokerdice.service.errors.AuthTokenError
import java.time.Instant
import com.pdm.pokerdice.repository.http.model.LoginOutputDto
import com.pdm.pokerdice.repository.http.model.MeOutputDto
import com.pdm.pokerdice.repository.http.model.UserOutputDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class HttpUserAuthService(
    private val client: HttpClient,
    private val authRepo: AuthInfoRepo
) : UserAuthService {

    private suspend fun getToken(): String? = authRepo.getAuthInfo()?.authToken

    override suspend fun getLoggedUser(): UserExternalInfo? {
        val token = getToken() ?: return null
        return try {
            val me = client.get("/api/me") {
                header("Authorization", "Bearer $token")
            }.body<MeOutputDto>()
            UserExternalInfo(me.id, me.name, me.balance)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createUser(
        name: String,
        email: String,
        password: String,
        invite: String
    ): Either<AuthTokenError, User> {
        return try {
            val response = client.post("/api/users") {
                setBody(mapOf(
                    "name" to name,
                    "email" to email,
                    "password" to password,
                    "invite" to invite
                ))
            }
            
            if (response.status.value !in 200..299) {
                return failure(AuthTokenError.UserNotFoundOrInvalidCredentials)
            }

            val userDto = response.body<UserOutputDto>()
            success(User(
                id = 0, 
                name = userDto.name,
                email = email,
                password = "",
                balance = userDto.balance,
                statistics = UserStatistics(0,0,0,0.0)
            ))
        } catch (e: Exception) {
            failure(AuthTokenError.UserNotFoundOrInvalidCredentials)
        }
    }

    override suspend fun createToken(
        email: String,
        password: String
    ): Either<AuthTokenError, TokenExternalInfo> {
        return try {
            val response = client.post("/api/users/token") {
                setBody(LoginInputDto(email, password))
            }
            
            if (response.status.value !in 200..299) {
                return failure(AuthTokenError.UserNotFoundOrInvalidCredentials)
            }

            val loginDto = response.body<LoginOutputDto>()
            val token = loginDto.token
            
            // Fetch user ID using the new token directly
            val meDto = client.get("/api/me") {
                header("Authorization", "Bearer $token")
            }.body<MeOutputDto>()
            
            authRepo.saveAuthInfo(AuthInfo(meDto.id, token))
            
            success(TokenExternalInfo(token, Instant.now().plusSeconds(3600)))
        } catch (e: Exception) {
            failure(AuthTokenError.UserNotFoundOrInvalidCredentials)
        }
    }

    override suspend fun getUserByToken(token: String): Either<AuthTokenError, User> {
        return try {
            val meDto = client.get("/api/me") {
                header("Authorization", "Bearer $token")
            }.body<MeOutputDto>()
            success(meDto.toDomain())
        } catch (e: Exception) {
            failure(AuthTokenError.TokenNotCreated)
        }
    }

    override suspend fun revokeToken(token: String): Boolean {
        return try {
            client.post("/api/users/logout") {
                header("Authorization", "Bearer $token")
            }
            authRepo.clearAuthInfo()
            true
        } catch (e: Exception) {
            authRepo.clearAuthInfo() 
            false
        }
    }
}
