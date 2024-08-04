package muoipt.nytopstories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import muoipt.nytopstories.navigation.BottomNavItem
import muoipt.nytopstories.navigation.NavigationGraph
import muoipt.nytopstories.ui.theme.NYTTheme

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NYTTheme {
                NYTApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NYTApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        if (backStackEntry?.destination?.route == BottomNavItem.Listing.screenRoute) stringResource(
            id = R.string.app_name
        ) else backStackEntry?.destination?.route
    val bottomNavVisibilityState = remember { (mutableStateOf(false)) }

    Scaffold(topBar = {
        NYTAppbar(currentScreenLabel = currentRoute,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = {
                navController.navigateUp()
            })
    }, bottomBar = {
        muoipt.nytopstories.navigation.BottomNavigation(
            navController = navController, bottomBarState = bottomNavVisibilityState
        )
    }) { innerPadding ->
        NavigationGraph(
            modifier = Modifier.padding(innerPadding), navController, bottomNavVisibilityState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NYTAppbar(
    currentScreenLabel: String?,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(title = { Text(currentScreenLabel ?: BottomNavItem.Listing.screenRoute) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        })
}
