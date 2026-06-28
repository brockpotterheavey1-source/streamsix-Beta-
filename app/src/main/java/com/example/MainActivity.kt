package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.SplashScreen
import com.example.ui.MainScreen
import com.example.ui.VerificationScreen
import com.example.ui.CmsScreen
import com.example.ui.SettingsScreen
import com.example.ui.PlayerScreen
import com.example.ui.theme.StreamSixTheme
import com.example.viewmodel.MainViewModel

enum class AppScreen {
    SPLASH,
    VERIFICATION,
    MAIN,
    CMS,
    SETTINGS,
    PLAYER
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      StreamSixTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val mainViewModel: MainViewModel = viewModel()
            var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }

            when (currentScreen) {
                AppScreen.SPLASH -> {
                    SplashScreen(
                        onServerReady = { discoveredUrl ->
                            // Here you would normally configure your singleton or NetworkModule
                            // For this demo, we proceed to verification
                            currentScreen = AppScreen.VERIFICATION
                        }
                    )
                }
                AppScreen.VERIFICATION -> {
                    VerificationScreen(
                        onVerified = { currentScreen = AppScreen.MAIN }
                    )
                }
                AppScreen.MAIN -> {
                    MainScreen(
                        viewModel = mainViewModel,
                        onNavigateToCms = { currentScreen = AppScreen.CMS },
                        onNavigateToSettings = { currentScreen = AppScreen.SETTINGS },
                        onNavigateToPlayer = { currentScreen = AppScreen.PLAYER }
                    )
                }
                AppScreen.CMS -> {
                    CmsScreen(
                        viewModel = mainViewModel,
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }
                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }
                AppScreen.PLAYER -> {
                    PlayerScreen(
                        videoUrls = listOf(
                            "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8", // Fallback 1
                            "http://sample.vodobox.net/skate_phantom_flex_4k/skate_phantom_flex_4k.m3u8" // Fallback 2
                        ),
                        onNavigateBack = { currentScreen = AppScreen.MAIN }
                    )
                }
            }
        }
      }
    }
  }
}
