package muoipt.nytopstories.ui.listing

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import muoipt.nytopstories.R
import muoipt.nytopstories.ui.base.BaseUi
import muoipt.nytopstories.ui.base.UIState
import muoipt.nytopstories.ui.components.CircleProgressBar

@Composable
fun ArticlesListingScreen(
    modifier: Modifier,
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

    BaseUi(content = { SetupUi(modifier, articlesList) }, snackBarMessage = getErrorMessage(state))
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
fun SetupUi(modifier: Modifier, articlesList: List<ArticleUiData>) {
    LazyColumn {
        articlesList.forEach { articleUiData ->
            item {
                Text(
                    modifier = modifier,
                    color = Color.Red,
                    text = articleUiData.title
                )
            }
        }
    }
}