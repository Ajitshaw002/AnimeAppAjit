package com.example.myapplicationnew.presenter.detail


import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AnimeDetailScreen(
    animeId: Int,
    onBack: () -> Unit,
    viewModel: AnimeDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    // Trigger load
    LaunchedEffect(animeId) {
        viewModel.loadDetail(animeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.anime?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                },
                actions = {}
            )
        }
    ) { padding ->
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            state.error != null -> Text("Error: ${state.error}")
            state.anime != null -> {
                // Show detail UI
                Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                    when {
                        state.isLoading && state.anime == null -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(48.dp) // ✅ smaller size
                                        .align(Alignment.Center)
                                )
                            }

                        }
                        state.error != null && state.anime == null -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = state.error ?: "Error", color = Color.Red)
                                Spacer(modifier = Modifier.height(8.dp))
                                //Button(onClick = { viewModel.loadDetail(true) }) { Text("Retry") }
                            }
                        }
                        state.anime != null -> {
                            val anime = state.anime!!
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    // Trailer or Poster
                                    if (!anime.trailerEmbedUrl.isNullOrBlank()) {
                                        AndroidView(
                                            factory = { ctx ->
                                                WebView(ctx).apply {
                                                    layoutParams = ViewGroup.LayoutParams(
                                                        MATCH_PARENT,
                                                        (220 * ctx.resources.displayMetrics.density).toInt()
                                                    )
                                                    settings.javaScriptEnabled = true
                                                    webViewClient = WebViewClient()
                                                    loadUrl(anime.trailerEmbedUrl)
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(220.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                    } else {
                                        AsyncImage(
                                            model = anime.imageUrl,
                                            contentDescription = anime.title,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(220.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                    }

                                }

                                item {
                                    Text(
                                        text = anime.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                item {
                                    Text("Rating: ${anime.score ?: "N/A"} | Episodes: ${anime.episodes ?: "?"}")
                                }

                                if (anime.genres.isNotEmpty()) {
                                    item {
                                        Text("Genres: ${anime.genres.joinToString()}")
                                    }
                                }

                                item {
                                    Text(
                                        text = anime.synopsis ?: "No synopsis available",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

    }
}
