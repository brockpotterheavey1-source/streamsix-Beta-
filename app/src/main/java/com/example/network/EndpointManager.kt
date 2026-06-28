package com.example.network

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

// Note: In a real Hilt setup, use @Singleton and @Inject
class EndpointManager {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    val fallbackUrls = listOf(
        "https://www.google.com", // Reliable for demo
        "https://popcornmovies.org",
        "https://api.yarrlist.net" // Example backup
    )

    suspend fun findActiveEndpoint(urls: List<String> = fallbackUrls): Result<String> = withContext(Dispatchers.IO) {
        try {
            coroutineScope {
                val channel = Channel<String?>(Channel.UNLIMITED)
                
                // Launch concurrent checks
                val jobs = urls.map { url ->
                    launch {
                        val result = checkEndpoint(url)
                        if (result != null) {
                            channel.send(result)
                        }
                    }
                }
                
                // Launch a supervisor to detect if ALL fail
                launch {
                    jobs.joinAll()
                    channel.send(null) // Signal completion if no success
                }
                
                val activeUrl = channel.receive()
                coroutineContext.cancelChildren() // Cancel remaining tasks once we have a winner
                
                if (activeUrl != null) {
                    Result.success(activeUrl)
                } else {
                    Result.failure(Exception("All servers are down or timed out."))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun checkEndpoint(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .head() // Use HEAD to minimize payload
            .build()
            
        return try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) url else null
            }
        } catch (e: IOException) {
            null
        }
    }
}
