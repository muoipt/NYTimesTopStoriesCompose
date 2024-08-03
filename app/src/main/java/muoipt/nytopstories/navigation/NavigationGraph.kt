package muoipt.nytopstories.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import muoipt.nytopstories.ui.bookmark.BookmarkListingScreen
import muoipt.nytopstories.ui.listing.ArticlesListingScreen

@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController,
    navVisible: MutableState<Boolean>
) {

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Listing.screenRoute,
    ) {
        composable(route = BottomNavItem.Listing.screenRoute) {
            navVisible.value = true
            ArticlesListingScreen(modifier = modifier)
        }

        composable(route = BottomNavItem.Bookmark.screenRoute) {
            navVisible.value = true
            BookmarkListingScreen(modifier = modifier)
        }
    }
}