package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class MainViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _filteredMovies = MutableStateFlow<List<Movie>>(emptyList())
    val filteredMovies: StateFlow<List<Movie>> = _filteredMovies.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre: StateFlow<String?> = _selectedGenre.asStateFlow()
    
    private val _selectedYear = MutableStateFlow<Int?>(null)
    val selectedYear: StateFlow<Int?> = _selectedYear.asStateFlow()

    private val _recommendedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val recommendedMovies: StateFlow<List<Movie>> = _recommendedMovies.asStateFlow()

    init {
        loadMockMovies()
    }

    private fun loadMockMovies() {
        val mockData = listOf(
            Movie("1", "The Matrix", "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GvwJwB02xcUK.jpg", "A computer hacker learns from mysterious rebels about the true nature of his reality.", 1999, "Action", 8.7, "HBO Max"),
            Movie("2", "Inception", "https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg", "A thief who steals corporate secrets through the use of dream-sharing technology.", 2010, "Sci-Fi", 8.8, "Netflix"),
            Movie("3", "Interstellar", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.", 2014, "Sci-Fi", 8.6, "Paramount+"),
            Movie("4", "The Dark Knight", "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg", "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham.", 2008, "Action", 9.0, "HBO Max"),
            Movie("5", "Pulp Fiction", "https://image.tmdb.org/t/p/w500/d5iIlFn5s0ImszYzBPb8SP443Z9.jpg", "The lives of two mob hitmen, a boxer, a gangster and his wife intertwine.", 1994, "Crime", 8.9, "Hulu"),
            Movie("6", "Fight Club", "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg", "An insomniac office worker and a devil-may-care soapmaker form an underground fight club.", 1999, "Drama", 8.8, "Popcorn"),
            Movie("7", "Forrest Gump", "https://image.tmdb.org/t/p/w500/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg", "The presidencies of Kennedy and Johnson, the events of Vietnam, Watergate, and other historical events unfold from the perspective of an Alabama man.", 1994, "Drama", 8.8, "Popcorn"),
            Movie("8", "The Shawshank Redemption", "https://image.tmdb.org/t/p/w500/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg", "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.", 1994, "Drama", 9.3, "Netflix")
        )
        _movies.value = mockData
        _filteredMovies.value = mockData
        updateRecommendations(mockData)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterMovies()
    }
    
    fun onGenreSelected(genre: String?) {
        _selectedGenre.value = genre
        filterMovies()
    }
    
    fun onYearSelected(year: Int?) {
        _selectedYear.value = year
        filterMovies()
    }
    
    private fun filterMovies() {
        val query = _searchQuery.value
        val genre = _selectedGenre.value
        val year = _selectedYear.value
        
        var currentList = _movies.value
        
        if (query.isNotBlank()) {
            currentList = currentList.filter { it.title.contains(query, ignoreCase = true) }
        }
        
        if (genre != null) {
            currentList = currentList.filter { it.genre.equals(genre, ignoreCase = true) }
        }
        
        if (year != null) {
            currentList = currentList.filter { it.releaseYear == year }
        }
        
        _filteredMovies.value = currentList
    }

    // CMS Functions
    fun addMovie(title: String, description: String, genre: String, releaseYear: Int, imageUrl: String) {
        val newMovie = Movie(
            id = UUID.randomUUID().toString(),
            title = title,
            overview = description,
            genre = genre,
            releaseYear = releaseYear,
            posterUrl = imageUrl,
            rating = 0.0 // Default rating
        )
        _movies.value = _movies.value + newMovie
        filterMovies()
        updateRecommendations(_movies.value)
    }

    fun deleteMovie(id: String) {
        _movies.value = _movies.value.filter { it.id != id }
        filterMovies()
        updateRecommendations(_movies.value)
    }

    // Recommendation Engine
    private fun updateRecommendations(allMovies: List<Movie>) {
        _recommendedMovies.value = allMovies.shuffled().take(4)
    }
}
