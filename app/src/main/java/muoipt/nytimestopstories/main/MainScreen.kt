package muoipt.nytimestopstories.main

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import muoipt.nytimestopstories.navigation.AppState
import muoipt.nytimestopstories.navigation.BottomNavigation
import muoipt.nytimestopstories.navigation.NavigationGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    appState: AppState
) {
    val bottomNavVisibilityState = remember { (mutableStateOf(false)) }

    Scaffold(
        bottomBar = { BottomNavigation(appState.navController, bottomNavVisibilityState) },
    ) {
        NavigationGraph(
            appState = appState,
            navVisible = bottomNavVisibilityState
        )
    }
}