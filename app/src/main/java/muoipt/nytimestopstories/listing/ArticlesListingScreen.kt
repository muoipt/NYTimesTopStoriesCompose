package muoipt.nytimestopstories.listing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ArticlesListingScreen(
    viewModel: ArticlesListingViewModel = hiltViewModel()
) {
    val state by viewModel.uiStates.collectAsStateWithLifecycle()
}