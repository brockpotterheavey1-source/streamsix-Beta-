package com.example.network

import com.example.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movies/trending")
    suspend fun getTrendingMovies(): MovieResponse

    @GET("movies/search")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("genre") genre: String? = null,
        @Query("year") year: Int? = null
    ): MovieResponse
}
