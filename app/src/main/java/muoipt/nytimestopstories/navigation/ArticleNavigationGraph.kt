package muoipt.nytimestopstories.navigation

import androidx.compose.runtime.MutableState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

sealed class ArticleRoute(val route: String) {
    object Root: ArticleRoute("root") // This is used as a target route to start article routes
    object Splash: ArticleRoute("splash")
    object Detail: ArticleRoute("Detail")
}

fun NavGraphBuilder.articleGraph(
    navVisible: MutableState<Boolean>,
) {
    navigation(
        route = ArticleRoute.Root.route,
        startDestination = ArticleRoute.Detail.route,
    ) {
        composable(
            route = ArticleRoute.Splash.route
        ) { backStackEntry ->
            navVisible.value = false

//            SplashScreen(
//
//            )
        }

        composable(
            route = ArticleRoute.Detail.route
        ) { backStackEntry ->
            navVisible.value = false

//            ArticleDetailScreen(
//
//            )
        }
    }
}