package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodel.SplashState
import com.example.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onServerReady: (String) -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "FlickyStream",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            when (uiState) {
                is SplashState.CheckingServers -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Text("Finding fastest streaming server...", color = MaterialTheme.colorScheme.onBackground)
                }
                is SplashState.ServerFound -> {
                    val url = (uiState as SplashState.ServerFound).url
                    Text("Connected to:", color = MaterialTheme.colorScheme.onBackground)
                    Text(url, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    
                    LaunchedEffect(Unit) {
                        delay(1000) // Brief pause to show the user it worked
                        onServerReady(url)
                    }
                }
                is SplashState.AllServersDown -> {
                    Text("All streaming servers are currently down.", color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.checkServers() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
