package com.example.myapplicationnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplicationnew.presenter.detail.AnimeDetailScreen
import com.example.myapplicationnew.presenter.list.AnimeListScreen
import com.example.myapplicationnew.ui.theme.MyApplicationNewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationNewTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.AnimeList.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Anime List
                        composable(Screen.AnimeList.route) {
                            AnimeListScreen(
                                onAnimeClick = { id ->
                                    navController.navigate(Screen.AnimeDetail.createRoute(id))
                                }
                            )
                        }

                        // Anime Detail
                        composable(
                            route = Screen.AnimeDetail.route,
                            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
                            AnimeDetailScreen(animeId = animeId,onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}


sealed class Screen(val route: String) {
    object AnimeList : Screen("anime_list")
    object AnimeDetail : Screen("anime_detail/{animeId}") {
        fun createRoute(animeId: Int) = "anime_detail/$animeId"
    }
}

