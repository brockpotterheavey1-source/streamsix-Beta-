package com.example.ui

import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.*
import coil.compose.AsyncImage
import com.example.model.Movie
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToCms: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {}
) {
    val movies by viewModel.filteredMovies.collectAsState()
    val groupedMovies = movies.groupBy { it.streamingService }
    var selectedNavIndex by remember { mutableStateOf(0) }

    // 5-7% overscan margin
    val overscanPadding = PaddingValues(horizontal = 48.dp, vertical = 27.dp)

    ModalNavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 27.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NavigationDrawerItem(
                    selected = selectedNavIndex == 0,
                    onClick = { selectedNavIndex = 0 },
                    leadingContent = { Icon(Icons.Default.Home, contentDescription = "Home") }
                ) { Text("Home") }
                NavigationDrawerItem(
                    selected = selectedNavIndex == 1,
                    onClick = onNavigateToCms,
                    leadingContent = { Icon(Icons.Default.Add, contentDescription = "CMS") }
                ) { Text("CMS") }
                NavigationDrawerItem(
                    selected = selectedNavIndex == 2,
                    onClick = onNavigateToSettings,
                    leadingContent = { Icon(Icons.Default.Settings, contentDescription = "Settings") }
                ) { Text("Settings") }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = overscanPadding
            ) {
                item {
                    HeroSectionTv(onPlayClicked = onNavigateToPlayer)
                    Spacer(modifier = Modifier.height(32.dp))
                }
                
                groupedMovies.forEach { (service, serviceMovies) ->
                    item {
                        CinematicCarousel(
                            title = service,
                            movies = serviceMovies,
                            onMovieClick = onNavigateToPlayer
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HeroSectionTv(onPlayClicked: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1626814026160-2237a95fc5a0?q=80&w=1000&auto=format&fit=crop",
            contentDescription = "Featured Movie",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF000000).copy(alpha = 0.9f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(32.dp)
        ) {
            Text(
                "DUNE: PART TWO",
                color = Color.White,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onPlayClicked) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Play Now")
                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.colors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("My List")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
val PivotBringIntoViewSpec = object : BringIntoViewSpec {
    override fun calculateScrollDistance(
        offset: Float,
        size: Float,
        containerSize: Float
    ): Float {
        val targetOffset = containerSize * 0.3f
        return offset - targetOffset
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CinematicCarousel(title: String, movies: List<Movie>, onMovieClick: () -> Unit) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        CompositionLocalProvider(LocalBringIntoViewSpec provides PivotBringIntoViewSpec) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(movies) { movie ->
                    TvMovieCard(movie = movie, onClick = onMovieClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvMovieCard(movie: Movie, onClick: () -> Unit = {}) {
    var isFocused by remember { mutableStateOf(false) }
    
    val alpha by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0.2f,
        animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing),
        label = "alpha"
    )
    val saturation by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0f,
        animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing),
        label = "saturation"
    )
    
    val colorMatrix = remember(saturation) {
        ColorMatrix().apply { setToSaturation(saturation) }
    }
    
    val glow = if (Build.VERSION.SDK_INT >= 28) {
        CardDefaults.glow(
            focusedGlow = Glow(
                elevationColor = MaterialTheme.colorScheme.primary,
                elevation = 16.dp
            )
        )
    } else {
        CardDefaults.glow()
    }
    
    val border = CardDefaults.border(
        focusedBorder = Border(
            border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp)
        )
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(160.dp)
            .aspectRatio(2f / 3f)
            .onFocusChanged { isFocused = it.isFocused },
        shape = CardDefaults.shape(shape = RoundedCornerShape(8.dp)),
        scale = CardDefaults.scale(focusedScale = 1.05f),
        glow = glow,
        border = border
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.alpha = alpha
                    },
                colorFilter = ColorFilter.colorMatrix(colorMatrix),
                contentScale = ContentScale.Crop
            )
            if (!isFocused) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
        }
    }
}
