package muoipt.nytimestopstories.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(
    appState: AppState,
    navVisible: MutableState<Boolean>
) {
    val navController = appState.navController

    NavHost(navController, startDestination = BottomNavItem.Listing.screenRoute) {
//        articleGraph(appState, navVisible)

        composable(
            route = BottomNavItem.Listing.screenRoute,
        ) { backStackEntry ->
            navVisible.value = true


        }

        composable(BottomNavItem.Bookmark.screenRoute) { backStackEntry ->
            navVisible.value = true

        }
    }
}