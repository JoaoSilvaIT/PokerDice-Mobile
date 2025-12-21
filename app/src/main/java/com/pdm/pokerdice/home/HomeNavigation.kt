package com.pdm.pokerdice.home

sealed class HomeNavigation {
    class LoginScreen : HomeNavigation()

    class SignUpScreen : HomeNavigation()
}
