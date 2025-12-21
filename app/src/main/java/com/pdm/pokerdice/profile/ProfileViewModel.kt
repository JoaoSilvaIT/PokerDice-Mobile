package com.pdm.pokerdice.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.pdm.pokerdice.domain.user.User
import com.pdm.pokerdice.domain.utilis.Either
import com.pdm.pokerdice.service.UserAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProfileScreenState {
    data object Loading : ProfileScreenState

    data class Success(
        val user: User,
    ) : ProfileScreenState

    data class Error(
        val message: String,
    ) : ProfileScreenState
}

class ProfileViewModel(
    private val service: UserAuthService,
) : ViewModel() {
    private val _state = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _state.value = ProfileScreenState.Loading
            when (val result = service.getUserInfo()) {
                is Either.Success -> {
                    _state.value = ProfileScreenState.Success(result.value)
                }
                is Either.Failure -> {
                    _state.value = ProfileScreenState.Error("Failed to load profile info")
                }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val token = service.getUserInfo() // Just to get current token if needed
            // Assuming revokeToken handles everything
            service.revokeToken("") // Token passed is ignored in our Http implementation for now as it reads from repo
            onSuccess()
        }
    }

    companion object {
        fun getFactory(service: UserAuthService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ProfileViewModel(service)
                }
            }
    }
}
