package com.pdm.pokerdice.service

interface HomeService {
    fun getCreators(): List<String>
}

class HomeServiceImpl : HomeService {
    override fun getCreators(): List<String> {
        return listOf(
            "Bernardo Jaco",
            "Pedro Monteiro",
            "João Silva"
        )
    }
}