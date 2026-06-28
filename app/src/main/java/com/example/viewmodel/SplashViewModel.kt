package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.EndpointManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashState {
    object CheckingServers : SplashState()
    data class ServerFound(val url: String) : SplashState()
    object AllServersDown : SplashState()
}

// Note: In a real Hilt setup, use @HiltViewModel and @Inject
class SplashViewModel(
    private val endpointManager: EndpointManager = EndpointManager() // Default provided for demo without Hilt
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashState>(SplashState.CheckingServers)
    val uiState: StateFlow<SplashState> = _uiState.asStateFlow()

    init {
        checkServers()
    }

    fun checkServers() {
        _uiState.value = SplashState.CheckingServers
        viewModelScope.launch {
            val result = endpointManager.findActiveEndpoint()
            
            if (result.isSuccess) {
                _uiState.value = SplashState.ServerFound(result.getOrNull() ?: "")
            } else {
                _uiState.value = SplashState.AllServersDown
            }
        }
    }
}
