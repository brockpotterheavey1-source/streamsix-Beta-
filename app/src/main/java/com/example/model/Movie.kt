package com.example.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String,
    val overview: String,
    val releaseYear: Int,
    val genre: String,
    val rating: Double,
    val streamingService: String = "Netflix",
    val streamUrl: String = "https://popcornmovies.org/stream"
)

@JsonClass(generateAdapter = true)
data class MovieResponse(
    val results: List<Movie>
)
