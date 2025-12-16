package com.pdm.pokerdice.login

import com.pdm.pokerdice.domain.UserInfo

interface LoginNavigation {
    fun goToHome()
    fun goToLobbies(userInfo : UserInfo)
}