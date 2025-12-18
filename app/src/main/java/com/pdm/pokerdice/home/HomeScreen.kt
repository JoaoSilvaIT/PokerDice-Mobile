package com.pdm.pokerdice.home

import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(onNavigate: (HomeNavigation) -> Unit = {}) {
    // Static data for the landing page
    val creators = listOf(
        "Pedro Monteiro (51457)",
        "João Silva (51682)",
        "Bernardo Jaco (51690)"
    )

    HomeView(
        creators = creators,
        onNavigate = onNavigate
    )
}
