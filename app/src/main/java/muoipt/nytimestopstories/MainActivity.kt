package muoipt.nytimestopstories

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import dagger.hilt.android.AndroidEntryPoint
import muoipt.nytimestopstories.base.BaseActivity
import muoipt.nytimestopstories.main.MainScreen
import muoipt.nytimestopstories.navigation.AppState
import muoipt.nytimestopstories.navigation.rememberAppState
import muoipt.nytimestopstories.ui.theme.NYTimesTopStoriesTheme

@AndroidEntryPoint
class MainActivity: BaseActivity() {

    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NYTimesTopStoriesTheme {
                App(navController = navController)
            }
        }
    }
}

@Composable
fun App(
    navController: NavHostController,
    appState: AppState = rememberAppState(navController)
) {
    MainScreen(appState)
}