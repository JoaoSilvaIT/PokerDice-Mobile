package com.pdm.pokerdice.ui.authentication

sealed class AuthenticationNavigation {
    class LobbiesScreen(val token : String) : AuthenticationNavigation()
}

