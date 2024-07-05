package muoipt.nytimestopstories.listing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import muoipt.nytimestopstories.R
import muoipt.nytimestopstories.base.BaseUi
import muoipt.nytimestopstories.base.UIState
import muoipt.nytimestopstories.ui.components.CircleProgressBar

@Composable
fun ArticlesListingScreen(
    viewModel: ArticlesListingViewModel = hiltViewModel()
) {
    val state by viewModel.uiStates.collectAsStateWithLifecycle()

    val articlesList = (state as? ArticlesListingUIState)?.articlesList ?: emptyList()

    when (state) {
        is ArticlesListingUIState.Default -> {
            LaunchedEffect(Unit) {
                viewModel.sendAction(ArticlesListingAction.LoadArticles)
            }
        }

        is ArticlesListingUIState.Loading -> {
            CircleProgressBar()
        }

        else -> {}
    }

    BaseUi(content = { SetupUi(articlesList) }, snackBarMessage = getErrorMessage(state))
}

@Composable
fun getErrorMessage(state: UIState): String? {
    val loadArticlesNotFoundErrorMsg = stringResource(id = R.string.error_no_articles)

    val errorMessage by remember(state) {
        derivedStateOf {
            if (state is ArticlesListingUIState.Error) {
                when (state.error.errorCode) {
                    ArticlesListingErrorCode.LoadArticlesNotFound -> loadArticlesNotFoundErrorMsg
                    else -> {
                        state.error.errorMessage
                    }
                }

            } else null
        }
    }

    return errorMessage
}

@Composable
fun SetupUi(articlesList: List<ArticleUiData>) {

}