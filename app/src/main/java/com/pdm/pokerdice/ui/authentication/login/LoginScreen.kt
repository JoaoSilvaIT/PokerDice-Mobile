package com.pdm.pokerdice.ui.authentication.login

sealed class LoginNavigation {
    class LobbiesScreen(val token : String) : LoginNavigation()
}