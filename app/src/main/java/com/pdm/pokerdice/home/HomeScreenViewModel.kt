package com.pdm.pokerdice.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface HomeScreenState {
    data class Success(val creators : List<String>) : HomeScreenState
}

class HomeScreenViewModel(
    private val service : HomeService
) : ViewModel() {

    companion object {
        /**
         * Returns a factory to create a [HomeScreenViewModel] with the provided parameters.
         */
        fun getFactory(service: HomeService
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
                    HomeScreenViewModel(
                        service
                    ) as T
                }
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}