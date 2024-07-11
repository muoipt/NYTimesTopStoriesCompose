package muoipt.nytimestopstories.navigation

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import muoipt.nytimestopstories.R

@ExperimentalAnimationApi
@Composable
fun BottomNavigation(navController: NavController, bottomBarState: MutableState<Boolean>) {
    val items = listOf(
        BottomNavItem.Listing,
        BottomNavItem.Bookmark
    )

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            androidx.compose.material.BottomNavigation(
                backgroundColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.black)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            item.icon?.let { painterResource(id = it) }?.let {
                                Icon(
                                    painter = it,
                                    contentDescription = item.title,
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 9.sp,
                                color = if (currentRoute == item.screenRoute) Color.Black else Color.Gray
                            )
                        },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray,
                        alwaysShowLabel = true,
                        selected = currentRoute == item.screenRoute,
                        onClick = {
                            if (currentRoute == item.screenRoute) {
                                return@BottomNavigationItem
                            }
                            val newRoute =
                                if (item.screenRoute == BottomNavItem.Listing.screenRoute) {
                                    BottomNavItem.Listing.screenRoute
                                } else {
                                    item.screenRoute
                                }
                            navController.navigate(newRoute) {
                                navController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(screen_route) { inclusive = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            navController.graph.setStartDestination(newRoute)
                        }
                    )
                }
            }
        }
    )
}