package com.example.fooditcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.common.theme.FoodItComposeTheme
import com.example.fooditcompose.navUtils.BottomNavItem
import com.example.fooditcompose.navUtils.BottomNavigationBar
import com.example.fooditcompose.ui.NavGraph
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodItComposeTheme {
                ProvideWindowInsets {
                    val navController = rememberAnimatedNavController()
                    // A surface container using the 'background' color from the theme
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        backgroundColor = MaterialTheme.colors.background,
                        bottomBar = {
                            when(currentRoute(navController)){
                                "/home", "/search", "/profile" -> {
                                    BottomNavigationBar(
                                        items = listOf(
                                            BottomNavItem(
                                                name = "Home",
                                                route = "/home",
                                                icon = Icons.Default.Home,
                                            ),
                                            BottomNavItem(
                                                name = "Search",
                                                route = "/search",
                                                icon = Icons.Default.Search,
                                            ),
                                            BottomNavItem(
                                                name = "Profile",
                                                route = "/profile",
                                                icon = Icons.Default.Settings,
                                            ),
                                        ),
                                        navController = navController,
                                        onItemClick ={
                                            navController.navigate(it.route)
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(
                            PaddingValues(0.dp, 0.dp, 0.dp, innerPadding.calculateBottomPadding()))) {
                                NavGraph(navController = navController)
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}
